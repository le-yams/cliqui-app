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
import com.mytdev.cliqui.cli.Argument;
import com.mytdev.cliqui.cli.Option;
import com.mytdev.cliqui.spi.CommandLineElementsUI;
import com.mytdev.cliqui.swing.SwingChangeSupport;
import com.mytdev.cliqui.util.MessageConsole;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Yann D'Isanto
 */
@Slf4j
public class CLIQUIPanel extends javax.swing.JPanel {

    private final CLIQUI<JPanel> cliqui;

    private File workingDirectory = new File("").getAbsoluteFile();

    private final JFileChooser fileChooser = new JFileChooser();

    private boolean running = false;

    public CLIQUIPanel(CLIQUI<JPanel> cliqui) {
        this.cliqui = cliqui;
        initComponents();
        execField.setText(cliqui.getCLI().getCommand());
        final CommandLineElementsUI<Option, JPanel> optionsUI = cliqui.getOptionsUI();
        final CommandLineElementsUI<Argument, JPanel> argsUI = cliqui.getArgumentsUI();
        if (optionsUI.getCommandLineElements().isEmpty()) {
            optionsPanel.setVisible(false);
        } else {
            optionsInnerPanel.add(optionsUI.getPanel(), BorderLayout.CENTER);
        }
        if (argsUI.getCommandLineElements().isEmpty()) {
            argsPanel.setVisible(false);
        } else {
            argsInnerPanel.add(argsUI.getPanel(), BorderLayout.CENTER);
        }
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        workingDirectoryField.setText(workingDirectory.getAbsolutePath());
        ((SwingChangeSupport) optionsUI.getChangeSupport()).addListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                validateCommandLineElementsUI(optionsUI);
            }
        });
        ((SwingChangeSupport) argsUI.getChangeSupport()).addListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                validateCommandLineElementsUI(argsUI);
            }
        });
    }

    private void validateCommandLineElementsUI(CommandLineElementsUI<?, JPanel> ui) {
        try {
            ui.validate();
            messageLabel.setText(" ");
            runButton.setEnabled(running == false);
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getLocalizedMessage());
            runButton.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        execField = new javax.swing.JLabel();
        optionsPanel = new javax.swing.JPanel();
        optionsInnerPanel = new javax.swing.JPanel();
        argsPanel = new javax.swing.JPanel();
        argsInnerPanel = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        workingDirectoryLabel = new javax.swing.JLabel();
        workingDirectoryField = new javax.swing.JTextField();
        browseWorkingDirectoryButton = new javax.swing.JButton();
        outputLabel = new javax.swing.JLabel();
        outputScrollPane = new javax.swing.JScrollPane();
        outputTextPane = new javax.swing.JTextPane();
        messageLabel = new javax.swing.JLabel();

        execField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        execField.setText("<<command>>");

        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

        optionsInnerPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionsInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionsInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        argsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Arguments"));

        argsInnerPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout argsPanelLayout = new javax.swing.GroupLayout(argsPanel);
        argsPanel.setLayout(argsPanelLayout);
        argsPanelLayout.setHorizontalGroup(
            argsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(argsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(argsInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        argsPanelLayout.setVerticalGroup(
            argsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(argsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(argsInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        workingDirectoryLabel.setText("Working directory");

        workingDirectoryField.setEditable(false);

        browseWorkingDirectoryButton.setText("Browse...");
        browseWorkingDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseWorkingDirectoryButtonActionPerformed(evt);
            }
        });

        outputLabel.setText("Output");

        outputTextPane.setEditable(false);
        outputScrollPane.setViewportView(outputTextPane);

        messageLabel.setForeground(new java.awt.Color(204, 0, 0));
        messageLabel.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(argsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(execField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(workingDirectoryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(workingDirectoryField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseWorkingDirectoryButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(runButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(outputLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(outputScrollPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(execField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(argsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(workingDirectoryLabel)
                    .addComponent(workingDirectoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseWorkingDirectoryButton))
                .addGap(18, 18, 18)
                .addComponent(outputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runButton)
                    .addComponent(messageLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        if (running) {
            return;
        }
        running = true;
        try {
            outputTextPane.setText("");
            final Process process = new ProcessBuilder(cliqui.getCommandLineValue())
                    .directory(workingDirectory)
                    .start();
            process.getOutputStream().close();
            new MessageConsole(outputTextPane)
                    .redirect(process.getInputStream())
                    .redirect(process.getErrorStream(), Color.RED);
            final Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        process.waitFor();
                    } catch (InterruptedException ex) {
                        log.error(ex.getLocalizedMessage(), ex);
                    } finally {
                        running = false;
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                runButton.setEnabled(true);
                            }
                        });
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    ex.getLocalizedMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            runButton.setEnabled(true);
            running = false;
        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void browseWorkingDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseWorkingDirectoryButtonActionPerformed
        fileChooser.setSelectedFile(workingDirectory);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            workingDirectory = fileChooser.getSelectedFile();
            workingDirectoryField.setText(workingDirectory.getAbsolutePath());
        }
    }//GEN-LAST:event_browseWorkingDirectoryButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel argsInnerPanel;
    private javax.swing.JPanel argsPanel;
    private javax.swing.JButton browseWorkingDirectoryButton;
    private javax.swing.JLabel execField;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPanel optionsInnerPanel;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JScrollPane outputScrollPane;
    private javax.swing.JTextPane outputTextPane;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField workingDirectoryField;
    private javax.swing.JLabel workingDirectoryLabel;
    // End of variables declaration//GEN-END:variables
}
