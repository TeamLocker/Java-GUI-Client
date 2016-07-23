/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.camerongray.mupl.gui_client;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import me.camerongray.mupl.core.*;

/**
 *
 * @author camerong
 */
public class MainWindow extends javax.swing.JFrame {

    private Locker locker;
    private User user;
    private HashMap<String, Folder> folderObjects = new HashMap<String, Folder>();
    private Folder selectedFolder;
    private ButtonColumn bcView;
    
    private HashMap<Integer, Integer> rowAccountIds = new HashMap<>();
    
    /**
     * Creates new form MainWindow
     */
    public MainWindow(Locker locker, User user) {
        this.locker = locker;
        this.user = user;
        initComponents();
        populateUi();
        
        
        // Table buttons - Column indices below must be kept up to date with the form
        final int VIEW_COLUMN_INDEX = 3;
        this.bcView = new ButtonColumn(tblAccounts,
            new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int accountId = rowAccountIds.get(Integer.valueOf(e.getActionCommand()));
                    System.out.println(accountId);
                }
            }
        ,VIEW_COLUMN_INDEX);
    }
    
    private void populateUi() {
        if (!this.user.isAdmin()) {
            this.menuNewFolder.setVisible(false);
            this.menuDeleteFolder.setVisible(false);
            this.menuEditFolder.setVisible(false);
            this.sepFolder.setVisible(false);
        }
        menuNewAccount.setEnabled(false);
        this.refreshFolderList();
    }
    
    public void refreshFolderList() {
        pgbStatus.setIndeterminate(true);
        lblStatus.setText("Getting folders...");
        (new GetFoldersTask(this, this.locker)).execute();
    }
    
    public void addFolderId(String name, Folder folder) {
        this.folderObjects.put(name, folder);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        pgbStatus = new javax.swing.JProgressBar();
        button1 = new java.awt.Button();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAccounts = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstFolders = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuNewAccount = new javax.swing.JMenuItem();
        sepFolder = new javax.swing.JPopupMenu.Separator();
        menuNewFolder = new javax.swing.JMenuItem();
        menuEditFolder = new javax.swing.JMenuItem();
        menuDeleteFolder = new javax.swing.JMenuItem();
        sepRefresh = new javax.swing.JPopupMenu.Separator();
        menuRefreshFolders = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multi-User Password Locker");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Ready");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pgbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button1.setLabel("button1");

        tblAccounts.setAutoCreateRowSorter(true);
        tblAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Username", "Notes", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAccounts.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblAccounts);
        if (tblAccounts.getColumnModel().getColumnCount() > 0) {
            tblAccounts.getColumnModel().getColumn(3).setMinWidth(100);
            tblAccounts.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblAccounts.getColumnModel().getColumn(3).setMaxWidth(100);
            tblAccounts.getColumnModel().getColumn(3).setCellEditor(this.bcView);
        }

        jSplitPane1.setRightComponent(jScrollPane2);

        lstFolders.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstFolders.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstFoldersValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstFolders);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jMenu1.setText("File");

        menuLogout.setText("Logout");
        menuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(menuLogout);

        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        jMenu1.add(menuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Actions");

        menuNewAccount.setText("New Account");
        menuNewAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewAccountActionPerformed(evt);
            }
        });
        jMenu2.add(menuNewAccount);
        jMenu2.add(sepFolder);

        menuNewFolder.setText("New Folder");
        menuNewFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewFolderActionPerformed(evt);
            }
        });
        jMenu2.add(menuNewFolder);

        menuEditFolder.setText("Edit Folder");
        menuEditFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditFolderActionPerformed(evt);
            }
        });
        jMenu2.add(menuEditFolder);

        menuDeleteFolder.setText("Delete Folder");
        menuDeleteFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeleteFolderActionPerformed(evt);
            }
        });
        jMenu2.add(menuDeleteFolder);
        jMenu2.add(sepRefresh);

        menuRefreshFolders.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        menuRefreshFolders.setText("Refresh Folders");
        menuRefreshFolders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRefreshFoldersActionPerformed(evt);
            }
        });
        jMenu2.add(menuRefreshFolders);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    private void menuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLogoutActionPerformed
        this.dispose();
        new Login(new javax.swing.JFrame(), true).setVisible(true);
    }//GEN-LAST:event_menuLogoutActionPerformed

    private void lstFoldersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstFoldersValueChanged
        if (!evt.getValueIsAdjusting()) {
            String folderName = lstFolders.getSelectedValue();
            if (folderName == null) {
                this.selectedFolder = null;
                menuNewAccount.setEnabled(false);
            } else {
                this.selectedFolder = this.folderObjects.get(folderName);
                menuNewAccount.setEnabled(this.selectedFolder.isWrite());
                pgbStatus.setIndeterminate(true);
                lblStatus.setText("Getting Accounts...");
                (new GetFolderAccountsTask(this, this.user, this.locker, this.selectedFolder)).execute();
            }
        }
    }//GEN-LAST:event_lstFoldersValueChanged

    private void menuRefreshFoldersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRefreshFoldersActionPerformed
        pgbStatus.setIndeterminate(true);
        lblStatus.setText("Getting folders...");
        (new GetFoldersTask(this, this.locker)).execute();
    }//GEN-LAST:event_menuRefreshFoldersActionPerformed

    private void menuNewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewFolderActionPerformed
        String folderName = (String)JOptionPane.showInputDialog(this, "Enter a name for this folder:",
                "New Folder", JOptionPane.QUESTION_MESSAGE);
        
        // Cancel button pressed
        if (folderName == null) {
            return;
        }
        
        try {
            this.locker.addFolder(folderName);
        } catch (LockerRuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                            "New Folder", JOptionPane.ERROR_MESSAGE);
            return;
        }
        pgbStatus.setIndeterminate(true);
        lblStatus.setText("Getting folders...");
        (new GetFoldersTask(this, this.locker)).execute();
    }//GEN-LAST:event_menuNewFolderActionPerformed

    private void menuDeleteFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeleteFolderActionPerformed
        // Folder ID copied elsewhere as this.selectedFolderId will reset to -1 when
        // we clear the selection in the folder list
        Folder folder = this.selectedFolder;
        if (folder == null)  {
            JOptionPane.showMessageDialog(this, "You must select a folder!",
                            "Delete Folder", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you "+
                "want to delete \""+lstFolders.getSelectedValue()+"\"?", "Delete Folder",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }
         
        
        pgbStatus.setIndeterminate(true);
        lblStatus.setText("Deleting folder...");
        lstFolders.clearSelection();
        try {
            locker.deleteFolder(folder.getId());
        } catch (LockerRuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                            "Delete Folder", JOptionPane.ERROR_MESSAGE);
        }
        
        lblStatus.setText("Getting folders...");
        (new GetFoldersTask(this, this.locker)).execute();
    }//GEN-LAST:event_menuDeleteFolderActionPerformed

    private void menuEditFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditFolderActionPerformed
        if (this.selectedFolder == null)  {
            JOptionPane.showMessageDialog(this, "You must select a folder!",
                            "Edit Folder", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        pgbStatus.setIndeterminate(true);
        lblStatus.setText("Fetching Permissions...");
        (new GetFolderPermissionsTask(this , this.locker, this.selectedFolder)).execute();
    }//GEN-LAST:event_menuEditFolderActionPerformed

    private void menuNewAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewAccountActionPerformed
        new AccountForm(this, true, AccountForm.NEW_MODE, this.locker, this.selectedFolder).setVisible(true);
    }//GEN-LAST:event_menuNewAccountActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JList<String> lstFolders;
    private javax.swing.JMenuItem menuDeleteFolder;
    private javax.swing.JMenuItem menuEditFolder;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuLogout;
    private javax.swing.JMenuItem menuNewAccount;
    private javax.swing.JMenuItem menuNewFolder;
    private javax.swing.JMenuItem menuRefreshFolders;
    private javax.swing.JProgressBar pgbStatus;
    private javax.swing.JPopupMenu.Separator sepFolder;
    private javax.swing.JPopupMenu.Separator sepRefresh;
    private javax.swing.JTable tblAccounts;
    // End of variables declaration//GEN-END:variables
    
    class GetFoldersTask extends SwingWorker<Folder[], Object> {
        private Locker locker;
        private MainWindow window;

        public GetFoldersTask(MainWindow window, Locker locker) {
            this.window = window;
            this.locker = locker;
        }
        
        @Override
        public Folder[] doInBackground() throws Exception {
            return this.locker.getFolders();
        }

        @Override
        public void done() {
            pgbStatus.setIndeterminate(false);
            lblStatus.setText("Ready");
            try {
                Folder[] folders = this.get();
                DefaultListModel model = new DefaultListModel();
                model.removeAllElements();
                for (int i = 0; i < folders.length; i++) {
                    model.addElement(folders[i].getName());
                    window.addFolderId(folders[i].getName(), folders[i]);
                }
                lstFolders.setModel(model);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this.window, e.getCause().getMessage(),
                            "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class GetFolderPermissionsTask extends SwingWorker<FolderPermission[], Object> {
        private Folder folder;
        private Locker locker;
        private MainWindow window;
        public GetFolderPermissionsTask(MainWindow window, Locker locker, Folder folder) {
            this.folder = folder;
            this.locker = locker;
            this.window = window;
        }
        
        @Override
        public FolderPermission[] doInBackground() throws Exception {
            return this.locker.getFolderPermissions(this.folder);
        }
        
        @Override
        public void done() {
            pgbStatus.setIndeterminate(false);
            lblStatus.setText("Ready");
            try {
                FolderPermission[] permissions = this.get();
                    new EditFolder(this.window, true, this.locker, this.folder, permissions).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this.window, e.getCause().getMessage(),
                            "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class GetFolderAccountsTask extends SwingWorker<Account[], Object> {
        private MainWindow window;
        private User user;
        private Locker locker;
        private Folder folder;

        public GetFolderAccountsTask(MainWindow window, User user, Locker locker, Folder folder) {
            this.window = window;
            this.user = user;
            this.locker = locker;
            this.folder = folder;
        }
        
        @Override
        public Account[] doInBackground() throws LockerRuntimeException {
            Account[] accounts = this.locker.getFolderAccounts(this.folder.getId(), this.user.getPrivateKey());
            return accounts;
        }
        
        @Override
        public void done() {
            pgbStatus.setIndeterminate(false);
            lblStatus.setText("Ready");
            try {
                Account[] accounts = this.get();
                
                DefaultTableModel model = (DefaultTableModel)tblAccounts.getModel();
                model.setRowCount(0); // Clear the table
                this.window.rowAccountIds.clear();
                int rowNum = 0;
                for (Account account : accounts) {    
                    model.addRow(new Object[]{account.getName(), account.getUsername(), account.getNotes(),
                        (this.window.user.isAdmin()) ? "View/Edit" : "View"});
                    this.window.rowAccountIds.put(rowNum, account.getId());
                    rowNum++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this.window, e.getMessage(),
                                "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}