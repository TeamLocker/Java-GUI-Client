/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.camerongray.teamlocker.core;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author camerong
 */
public class User {
    private int id;
    private String fullName;
    private String username;
    private String email;
    private byte[] publicKey;
    private byte[] encryptedPrivateKey;
    private boolean admin;
    private byte[] pbkdf2Salt;
    private byte[] aesIv;
    private String authHash;
    private byte[] privateKey;
    private boolean isCurrentUser;
    private String password;
    
    public User() {}

    public User(String fullName, String username, String email, String password, boolean admin) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.admin = admin;
        this.password = password;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PublicKey getPublicKey() {
        return new PublicKey(this.id, this.publicKey);
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(byte[] encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }
    
    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = Base64.getDecoder().decode(encryptedPrivateKey);
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public byte[] getPbkdf2Salt() {
        return pbkdf2Salt;
    }

    public void setPbkdf2Salt(byte[] pbkdf2Salt) {
        this.pbkdf2Salt = pbkdf2Salt;
    }
    
    public void setPbkdf2Salt(String pbkdf2Salt) {
        this.pbkdf2Salt = Base64.getDecoder().decode(pbkdf2Salt);
    }

    public byte[] getAesIv() {
        return aesIv;
    }

    public void setAesIv(byte[] aesIv) {
        this.aesIv = aesIv;
    }
    
    public void setAesIv(String aesIv) {
        this.aesIv = Base64.getDecoder().decode(aesIv);
    }

    public String getAuthHash() {
        return authHash;
    }

    public void setAuthHash(String authHash) {
        this.authHash = authHash;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public void setIsCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

    public boolean isIsCurrentUser() {
        return isCurrentUser;
    }
    
    public static User getCurrentFromServer() throws IOException, LockerCommunicationException, CryptoException, LockerSimpleException  {
        Locker locker = Locker.getInstance();
        String response = locker.makeGetRequest("users/self").getJSONObject("user").toString();

        User user = locker.getObjectMapper().readValue(response, User.class);
        user.setIsCurrentUser(true);

        // Decrypts the private key
        user.setPrivateKey(Crypto.aesDecrypt(locker.getPassword(), user.getPbkdf2Salt(), user.getAesIv(), user.getEncryptedPrivateKey()));

        return user;
    }
    
    public static User getFromServer(int userId) throws IOException, LockerCommunicationException, CryptoException, LockerSimpleException  {
        Locker locker = Locker.getInstance();
        String response = locker.makeGetRequest("users/"+userId).getJSONObject("user").toString();

        User user = locker.getObjectMapper().readValue(response, User.class);
        user.setIsCurrentUser(false);

        // Decrypts the private key
        user.setPrivateKey(Crypto.aesDecrypt(locker.getPassword(), user.getPbkdf2Salt(), user.getAesIv(), user.getEncryptedPrivateKey()));

        return user;
    }
    
    public static User[] getAllFromServer() throws LockerSimpleException, IOException, LockerCommunicationException {
        Locker locker = Locker.getInstance();
        JSONObject response = locker.makeGetRequest("users");

        if (!response.isNull("error")) {
            throw new LockerSimpleException(response.getString("message"));
        }

        User[] users = locker.getObjectMapper().readValue(response.getJSONArray("users").toString(), User[].class);

        return users;
    }
    
    public void changePasswordOnServer(String newPassword) throws LockerSimpleException, CryptoException, LockerCommunicationException, LockerRuntimeException {
        if (newPassword.isEmpty()) {
            throw new LockerRuntimeException("You must enter a password!");
        }
        
        if (!this.isCurrentUser) {
            throw new LockerRuntimeException("You can only change the password for your own user!");
        }
        
        Locker locker = Locker.getInstance();

        Crypto.EncryptedPrivateKey encryptedPrivateKey = Crypto.encryptPrivateKey(newPassword, this.privateKey);

        String newAuthKey = Crypto.generateAuthKey(newPassword, this.username);

        Base64.Encoder encoder = Base64.getEncoder();

        String payload = (new JSONObject()
                .put("encrypted_private_key", new String(encoder.encode(encryptedPrivateKey.getKey())))
                .put("aes_iv", new String(encoder.encode(encryptedPrivateKey.getIv())))
                .put("pbkdf2_salt", new String(encoder.encode(encryptedPrivateKey.getSalt())))
                .put("auth_key", newAuthKey)).toString();

        JSONObject response = locker.makePostRequest("users/self/update_password", payload);

        if (!response.isNull("error")) {
            throw new LockerSimpleException(response.getString("message"));
        }
    }
    
    public void updateOnServer() throws LockerSimpleException, CryptoException, LockerCommunicationException, LockerRuntimeException {       
        Locker locker = Locker.getInstance();
        
        String payload = (new JSONObject()
                .put("username", this.username)
                .put("full_name", this.fullName)
                .put("email", this.email)
                .put("admin", this.admin)).toString();

        JSONObject response = locker.makePostRequest("users/"+this.id, payload);

        if (!response.isNull("error")) {
            throw new LockerSimpleException(response.getString("message"));
        }
    }
    
    public User addToServer() throws LockerSimpleException, CryptoException, IOException, LockerCommunicationException, LockerRuntimeException {
        Validation.ensureNonEmpty(this.username, "Username");
        Validation.ensureNonEmpty(this.password, "Password");
        Validation.ensureNonEmpty(this.fullName, "Full Name");
        Validation.ensureNonEmpty(this.email, "Email");
        
        Locker locker = Locker.getInstance();
        
        KeyPair keypair = Crypto.generateRsaKeyPair();
        this.privateKey = keypair.getPrivate().getEncoded();
        this.publicKey = keypair.getPublic().getEncoded();
        Crypto.EncryptedPrivateKey epk = Crypto.encryptPrivateKey(this.password, privateKey);
        this.encryptedPrivateKey = epk.getKey();
        this.aesIv = epk.getIv();
        this.pbkdf2Salt = epk.getSalt();
        String authKey = Crypto.generateAuthKey(this.password, this.username);

        Base64.Encoder encoder = Base64.getEncoder();

        String payload = (new JSONObject()
                .put("encrypted_private_key", new String(encoder.encode(this.encryptedPrivateKey)))
                .put("aes_iv", new String(encoder.encode(this.getAesIv())))
                .put("pbkdf2_salt", new String(encoder.encode(this.getPbkdf2Salt())))
                .put("public_key", new String(encoder.encode(this.publicKey)))
                .put("auth_key", authKey)
                .put("username", this.username)
                .put("full_name", this.fullName)
                .put("email", this.email)
                .put("admin", this.admin)).toString();

        JSONObject response = locker.makePutRequest("users", payload);

        if (!response.isNull("error")) {
            throw new LockerSimpleException(response.getString("message"));
        }

        this.id = response.getInt("user_id");

        return this;
    }
    
    // TODO - Why is this static?
    public static FolderPermission[] getPermissionsFromServer(User user) throws IOException, LockerCommunicationException, CryptoException, LockerSimpleException  {
        Locker locker = Locker.getInstance();
        JSONArray permissions = locker.makeGetRequest("users/"+user.getId()+"/permissions").getJSONArray("permissions");

        ArrayList<FolderPermission> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length(); i++) {
            JSONObject permission = permissions.getJSONObject(i);
            permissionList.add(new FolderPermission(user, new Folder(permission.getInt("folder_id")), permission.getBoolean("read"), permission.getBoolean("write")));
        }
        return permissionList.toArray(new FolderPermission[permissionList.size()]);
    }
    
    public void deletePermissionsFromServer() throws LockerSimpleException, LockerCommunicationException {
        Locker locker = Locker.getInstance();
        
        JSONObject response = locker.makeDeleteRequest("users/"+this.id+"/permissions");
        
        if (!response.isNull("error")) {
            throw new LockerSimpleException(response.getString("message"));
        }
    }
}
