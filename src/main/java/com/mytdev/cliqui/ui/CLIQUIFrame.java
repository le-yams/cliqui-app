/*
 * Copyright 2014 Yann D'Isanto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mytdev.cliqui.ui;

import com.mytdev.cliqui.CLIQUI;
import com.mytdev.cliqui.model.CLIEntry;
import com.mytdev.cliqui.model.CLIEntryRepository;
import com.mytdev.cliqui.resources.Images;
import com.mytdev.cliqui.util.ButtonTabComponent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Yann D'Isanto
 */
@Slf4j
public class CLIQUIFrame extends javax.swing.JFrame {

    private final CLIEntryRepository cliEntryRepository;

    private final CLIListModel cliListModel = new CLIListModel();

    private final Action importCLIAction = new ImportCLIAction();

    private final Action exitAction = new ExitAction();

    private final Map<String, CLIQUIPanel> panels = new HashMap<>();

    public CLIQUIFrame(CLIEntryRepository cliEntryRepository) {
        initComponents();
        setLocationRelativeTo(null);
        this.cliEntryRepository = cliEntryRepository;
        try {
            cliEntryRepository.load();
            for (CLIEntry entry : cliEntryRepository.getEntries()) {
                cliListModel.addEntry(entry);
            }
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            JOptionPane.showMessageDialog(CLIQUIFrame.this,
                ex.getLocalizedMessage(),
                "Error loading user defined UIs",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCLIEntryPanel(CLIEntry entry) {
        final String name = entry.getName();
        CLIQUIPanel panel = panels.get(name);
        if (panel == null) {
            panel = new CLIQUIPanel(CLIQUI.swing(entry.getCLI()));
            panels.put(name, panel);
            tabbedPane.addTab(name, panel);
            final int index = tabbedPane.indexOfComponent(panel);
            tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
        }
        tabbedPane.setSelectedComponent(panel);
    }

    private final class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }

    }

    private final class ImportCLIAction extends AbstractAction {

        public ImportCLIAction() {
            super("Import UI", new ImageIcon(Images.IMPORT));
            putValue(SHORT_DESCRIPTION, "Import CLI Quick UI definition file");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ImportCLIDialog dialog = new ImportCLIDialog(CLIQUIFrame.this);
            dialog.setVisible(true);
            if (dialog.getReturnStatus() == ImportCLIDialog.RET_OK) {
                final String name = dialog.getNameFieldValue();
                final File file = dialog.getSelectedFile();
                try {
                    final CLIEntry entry = cliEntryRepository.importFile(name, file);
                    cliListModel.addEntry(entry);
                    if(dialog.isOpenPanelSelected()) {
                        openCLIEntryPanel(entry);
                    }
                } catch (IOException ex) {
                    log.error(ex.getLocalizedMessage(), ex);
                    JOptionPane.showMessageDialog(CLIQUIFrame.this,
                        ex.getLocalizedMessage(),
                        "Error importing file",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private final class RemoveCLIEntryAction extends AbstractAction {

        private final CLIEntry entry;

        private final int index;

        public RemoveCLIEntryAction(CLIEntry entry, int index) {
            super("Remove");
            this.entry = entry;
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final String entryName = entry.getName();
                JOptionPane.showConfirmDialog(CLIQUIFrame.this,
                    "delete entry " + entryName + "?",
                    "confirmation",
                    JOptionPane.YES_NO_OPTION);
                if (cliEntryRepository.removeEntry(entryName)) {
                    cliListModel.removeEntry(index);
                    final CLIQUIPanel panel = panels.get(entryName);
                    if (panel != null) {
                        tabbedPane.remove(panel);
                    }
                }
            } catch (IOException ex) {
                log.error(ex.getLocalizedMessage(), ex);
                JOptionPane.showMessageDialog(CLIQUIFrame.this,
                    ex.getLocalizedMessage(),
                    "Error deleting file",
                    JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    private JPopupMenu buildListPopup(int index, CLIEntry entry) {
        final JPopupMenu menu = new JPopupMenu();
        menu.add(new JMenuItem(new RemoveCLIEntryAction(entry, index)));
        return menu;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        cliListScrollPane = new javax.swing.JScrollPane();
        cliList = new javax.swing.JList<CLIEntry>();
        tabbedPane = new javax.swing.JTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importCLIMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CLI Quick UI");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        splitPane.setResizeWeight(0.1);

        cliList.setModel(cliListModel);
        cliList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cliListMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cliListMouseClicked(evt);
            }
        });
        cliListScrollPane.setViewportView(cliList);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cliListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cliListScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(tabbedPane);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setAction(importCLIAction);
        jButton1.setFocusable(false);
        jButton1.setHideActionText(true);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        fileMenu.setText("File");

        importCLIMenuItem.setAction(importCLIAction);
        fileMenu.add(importCLIMenuItem);
        fileMenu.add(jSeparator1);

        exitMenuItem.setAction(exitAction);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("?");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 952, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cliListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cliListMouseClicked
        final CLIEntry entry = cliList.getSelectedValue();
        if (entry != null && evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            openCLIEntryPanel(entry);
        }
    }//GEN-LAST:event_cliListMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitAction.actionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void cliListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cliListMouseReleased
        if (evt.isPopupTrigger()) {
            final int index = cliList.locationToIndex(evt.getPoint());
            if (index > -1) {
                if (index != cliList.getSelectedIndex()) {
                    cliList.setSelectedIndex(index);
                }
                final CLIEntry entry = cliList.getSelectedValue();
                buildListPopup(index, entry).show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_cliListMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<CLIEntry> cliList;
    private javax.swing.JScrollPane cliListScrollPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importCLIMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
