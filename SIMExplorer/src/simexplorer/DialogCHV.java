/*
 * The MIT License
 *
 * Copyright 2018 Gustavo Rabelo <gustavo.vrr@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package simexplorer;

import simexplorer.apdusender.APDUSender;
import simexplorer.files.DF;
import javax.swing.JOptionPane;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class DialogCHV extends javax.swing.JDialog {

    private final APDUSender apduSender;
    private DF dfMaster = null;
    
    /**
     * Creates new form DialogCHV
     */
    public DialogCHV(java.awt.Frame parent, boolean modal, APDUSender apduSender) {
        super(parent, modal);
        this.apduSender = apduSender;
        initComponents();
        
        alterarControles();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        pnlVerify = new javax.swing.JPanel();
        edtVerifyCHV = new javax.swing.JTextField();
        btnVerifyCHV = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnlChange = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        edtOldCHV = new javax.swing.JTextField();
        btnChangeCHV = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        edtNewCHVChange = new javax.swing.JTextField();
        pnlDisable = new javax.swing.JPanel();
        edtDisableCHV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnDisableCHV = new javax.swing.JButton();
        pnlEnable = new javax.swing.JPanel();
        edtEnableCHV = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnEnableCHV = new javax.swing.JButton();
        pnlUnblock = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        edtPUK = new javax.swing.JTextField();
        edtNewCHVPUK = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnUnblockCHV = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        rdbCHV1 = new javax.swing.JRadioButton();
        rdbCHV2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CHV Management");

        btnVerifyCHV.setText("Verify CHV");
        btnVerifyCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerifyCHVActionPerformed(evt);
            }
        });

        jLabel1.setText("CHV:");

        javax.swing.GroupLayout pnlVerifyLayout = new javax.swing.GroupLayout(pnlVerify);
        pnlVerify.setLayout(pnlVerifyLayout);
        pnlVerifyLayout.setHorizontalGroup(
            pnlVerifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVerifyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVerifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVerifyLayout.createSequentialGroup()
                        .addComponent(btnVerifyCHV)
                        .addGap(0, 682, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVerifyLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtVerifyCHV)))
                .addContainerGap())
        );
        pnlVerifyLayout.setVerticalGroup(
            pnlVerifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVerifyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVerifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtVerifyCHV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnVerifyCHV)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Verify", pnlVerify);

        jLabel2.setText("Old CHV:");

        btnChangeCHV.setText("Change CHV");
        btnChangeCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeCHVActionPerformed(evt);
            }
        });

        jLabel3.setText("New CHV: ");

        javax.swing.GroupLayout pnlChangeLayout = new javax.swing.GroupLayout(pnlChange);
        pnlChange.setLayout(pnlChangeLayout);
        pnlChangeLayout.setHorizontalGroup(
            pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChangeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChangeLayout.createSequentialGroup()
                        .addComponent(btnChangeCHV)
                        .addGap(0, 672, Short.MAX_VALUE))
                    .addGroup(pnlChangeLayout.createSequentialGroup()
                        .addGroup(pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtOldCHV)
                            .addComponent(edtNewCHVChange))))
                .addContainerGap())
        );
        pnlChangeLayout.setVerticalGroup(
            pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChangeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtOldCHV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(8, 8, 8)
                .addGroup(pnlChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtNewCHVChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChangeCHV)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Change", pnlChange);

        jLabel4.setText("CHV:");

        btnDisableCHV.setText("Disable CHV");
        btnDisableCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisableCHVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDisableLayout = new javax.swing.GroupLayout(pnlDisable);
        pnlDisable.setLayout(pnlDisableLayout);
        pnlDisableLayout.setHorizontalGroup(
            pnlDisableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDisableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDisableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDisableLayout.createSequentialGroup()
                        .addComponent(btnDisableCHV)
                        .addGap(0, 676, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDisableLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtDisableCHV)))
                .addContainerGap())
        );
        pnlDisableLayout.setVerticalGroup(
            pnlDisableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDisableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDisableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtDisableCHV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDisableCHV)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Disable", pnlDisable);

        jLabel5.setText("CHV:");

        btnEnableCHV.setText("Enable CHV");
        btnEnableCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnableCHVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEnableLayout = new javax.swing.GroupLayout(pnlEnable);
        pnlEnable.setLayout(pnlEnableLayout);
        pnlEnableLayout.setHorizontalGroup(
            pnlEnableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEnableLayout.createSequentialGroup()
                        .addComponent(btnEnableCHV)
                        .addGap(0, 678, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnableLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtEnableCHV)))
                .addContainerGap())
        );
        pnlEnableLayout.setVerticalGroup(
            pnlEnableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtEnableCHV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEnableCHV)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Enable", pnlEnable);

        jLabel7.setText("UNBLOCK CHV (PUK):");

        jLabel8.setText("New CHV: ");

        btnUnblockCHV.setText("Unblock CHV");
        btnUnblockCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnblockCHVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlUnblockLayout = new javax.swing.GroupLayout(pnlUnblock);
        pnlUnblock.setLayout(pnlUnblockLayout);
        pnlUnblockLayout.setHorizontalGroup(
            pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUnblockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlUnblockLayout.createSequentialGroup()
                        .addComponent(btnUnblockCHV)
                        .addGap(0, 672, Short.MAX_VALUE))
                    .addGroup(pnlUnblockLayout.createSequentialGroup()
                        .addGroup(pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtPUK)
                            .addComponent(edtNewCHVPUK))))
                .addContainerGap())
        );
        pnlUnblockLayout.setVerticalGroup(
            pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUnblockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtPUK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(8, 8, 8)
                .addGroup(pnlUnblockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtNewCHVPUK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUnblockCHV)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Unblock (PUK)", pnlUnblock);

        btnFechar.setText("Close");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        txtArea.setColumns(20);
        txtArea.setRows(5);
        txtArea.setText("CHV1 status-> Number of false presentations remaining ('0' means blocked):\nCHV1 status-> \nUNBLOCK CHV1 status-> Number of false presentations remaining ('0' means blocked): \nUNBLOCK CHV1 status-> \nCHV2 status-> Number of false presentations remaining ('0' means blocked): \nCHV2 status-> \nUNBLOCK CHV2 status-> Number of false presentations remaining ('0' means blocked): \nUNBLOCK CHV2 status-> ");
        txtArea.setEnabled(false);
        jScrollPane1.setViewportView(txtArea);

        buttonGroup.add(rdbCHV1);
        rdbCHV1.setSelected(true);
        rdbCHV1.setText("CHV1 (PIN)");
        rdbCHV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbCHV1ActionPerformed(evt);
            }
        });

        buttonGroup.add(rdbCHV2);
        rdbCHV2.setText("CHV2 (PIN2)");
        rdbCHV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbCHV2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnFechar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdbCHV2)
                            .addComponent(rdbCHV1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbCHV1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbCHV2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFechar)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdbCHV2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbCHV2ActionPerformed
        
        alterarControles();
        
    }//GEN-LAST:event_rdbCHV2ActionPerformed

    private void rdbCHV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbCHV1ActionPerformed
        alterarControles();
    }//GEN-LAST:event_rdbCHV1ActionPerformed

    private void btnVerifyCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerifyCHVActionPerformed

        
        String chv = removeChars(edtVerifyCHV.getText());
        if(chv.length()==0 || chv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "CHV must contains up to 8 characters.");
            return;
        }
        
        byte[] apdu = new byte[13];
        apdu[0] = (byte)0xa0;
        apdu[1] = (byte)0x20;
        apdu[2] = 0;
        apdu[3] = rdbCHV1.isSelected()?(byte)1:(byte)2;
        apdu[4] = 8;
        for(int i=0; i<8; i++)
        {
            if(chv.length()>i)
            {
                apdu[5+i] =  (byte)chv.charAt(i);
            }
            else
            {
                apdu[5+i] = (byte)0xFF;
            }
            
        }
        
        
        
        
        byte[] resposta = apduSender.enviarAPDU(apdu);
        if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-2] == (byte)0x90)
            JOptionPane.showMessageDialog(null, "Success.");
        else
            JOptionPane.showMessageDialog(null, "Fail.");
            
        
        alterarControles();
        
        
    }//GEN-LAST:event_btnVerifyCHVActionPerformed

    private String removeChars(String s)
    {
        return s.replaceAll("[\\p{C}\\s\n\t]","?");
    }
    
    private void btnEnableCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnableCHVActionPerformed
        
        String chv = removeChars(edtEnableCHV.getText());
        if(chv.length()==0 || chv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "PUK must contains up to 8 characters.");
            return;
        }
        
        byte[] apdu = new byte[13];
        apdu[0] = (byte)0xa0;
        apdu[1] = (byte)0x28;
        apdu[2] = 0;
        apdu[3] = 1;
        apdu[4] = 0x8;
        for(int i=0; i<8; i++)
        {
            if(chv.length()>i)
            {
                apdu[5+i] =  (byte)chv.charAt(i);
            }
            else
            {
                apdu[5+i] = (byte)0xFF;
            }
            
        }
        
        
        
        
        byte[] resposta = apduSender.enviarAPDU(apdu);
        if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-2] == (byte)0x90)
            JOptionPane.showMessageDialog(null, "Success.");
        else
            JOptionPane.showMessageDialog(null, "Fail.");
            
        
        alterarControles();
        
    }//GEN-LAST:event_btnEnableCHVActionPerformed

    private void btnDisableCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisableCHVActionPerformed

        String chv = removeChars(edtDisableCHV.getText());
        if(chv.length()==0 || chv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "CHV must contains up to 8 characters.");
            return;
        }
        
        byte[] apdu = new byte[13];
        apdu[0] = (byte)0xa0;
        apdu[1] = (byte)0x26;
        apdu[2] = 0;
        apdu[3] = 1;
        apdu[4] = 8;
        for(int i=0; i<8; i++)
        {
            if(chv.length()>i)
            {
                apdu[5+i] =  (byte)chv.charAt(i);
            }
            else
            {
                apdu[5+i] = (byte)0xFF;
            }
            
        }
        
        
        
        
        byte[] resposta = apduSender.enviarAPDU(apdu);
        if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-2] == (byte)0x90)
            JOptionPane.showMessageDialog(null, "Success.");
        else
            JOptionPane.showMessageDialog(null, "Fail.");
            
        
        alterarControles();
        
        
        
    }//GEN-LAST:event_btnDisableCHVActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnChangeCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeCHVActionPerformed

        
        String oldchv = removeChars(edtOldCHV.getText());
        if(oldchv.length()==0 || oldchv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "CHV must contains up to 8 characters.");
            return;
        }

        String newchv = removeChars(edtNewCHVChange.getText());
        if(newchv.length()==0 || newchv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "CHV must contains up to 8 characters.");
            return;
        }
        
        
        byte[] apdu = new byte[21];
        apdu[0] = (byte)0xa0;
        apdu[1] = (byte)0x24;
        apdu[2] = 0;
        apdu[3] = rdbCHV1.isSelected()? (byte)1:(byte)2;
        apdu[4] = 0x10;
        for(int i=0; i<8; i++)
        {
            if(oldchv.length()>i)
            {
                apdu[5+i] =  (byte)oldchv.charAt(i);
            }
            else
            {
                apdu[5+i] = (byte)0xFF;
            }
            
        }
        for(int i=0; i<8; i++)
        {
            if(newchv.length()>i)
            {
                apdu[13+i] =  (byte)newchv.charAt(i);
            }
            else
            {
                apdu[13+i] = (byte)0xFF;
            }
            
        }
        
        
        
        
        byte[] resposta = apduSender.enviarAPDU(apdu);
        if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-2] == (byte)0x90)
            JOptionPane.showMessageDialog(null, "Success.");
        else
            JOptionPane.showMessageDialog(null, "Fail.");
            
        
        alterarControles();
                
        
        
    }//GEN-LAST:event_btnChangeCHVActionPerformed

    private void btnUnblockCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnblockCHVActionPerformed

        String puk = removeChars(edtPUK.getText());
        if(puk.length()==0 || puk.length()>8)
        {
            JOptionPane.showMessageDialog(null, "PUK must contains up to 8 characters.");
            return;
        }

        String newchv = removeChars(edtNewCHVPUK.getText());
        if(newchv.length()==0 || newchv.length()>8)
        {
            JOptionPane.showMessageDialog(null, "CHV must contains up to 8 characters.");
            return;
        }
        
        
        byte[] apdu = new byte[21];
        apdu[0] = (byte)0xa0;
        apdu[1] = (byte)0x2c;
        apdu[2] = 0;
        apdu[3] = rdbCHV1.isSelected()? (byte)0:(byte)2;
        apdu[4] = 0x10;
        for(int i=0; i<8; i++)
        {
            if(puk.length()>i)
            {
                apdu[5+i] =  (byte)puk.charAt(i);
            }
            else
            {
                apdu[5+i] = (byte)0xFF;
            }
            
        }
        for(int i=0; i<8; i++)
        {
            if(newchv.length()>i)
            {
                apdu[13+i] =  (byte)newchv.charAt(i);
            }
            else
            {
                apdu[13+i] = (byte)0xFF;
            }
            
        }
        
        
        
        
        byte[] resposta = apduSender.enviarAPDU(apdu);
        if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-2] == (byte)0x90)
            JOptionPane.showMessageDialog(null, "Success.");
        else
            JOptionPane.showMessageDialog(null, "Fail.");
            
        
        alterarControles();
                
        
        
        
    }//GEN-LAST:event_btnUnblockCHVActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangeCHV;
    private javax.swing.JButton btnDisableCHV;
    private javax.swing.JButton btnEnableCHV;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnUnblockCHV;
    private javax.swing.JButton btnVerifyCHV;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JTextField edtDisableCHV;
    private javax.swing.JTextField edtEnableCHV;
    private javax.swing.JTextField edtNewCHVChange;
    private javax.swing.JTextField edtNewCHVPUK;
    private javax.swing.JTextField edtOldCHV;
    private javax.swing.JTextField edtPUK;
    private javax.swing.JTextField edtVerifyCHV;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlDisable;
    private javax.swing.JPanel pnlEnable;
    private javax.swing.JPanel pnlUnblock;
    private javax.swing.JPanel pnlVerify;
    private javax.swing.JRadioButton rdbCHV1;
    private javax.swing.JRadioButton rdbCHV2;
    private javax.swing.JTextArea txtArea;
    // End of variables declaration//GEN-END:variables

    private void alterarControles() {

        
        try {
            dfMaster = new DF(apduSender,"MF", new String[0]);
        } catch (SIMFileNotFoundException ex) {
            
        }
        
        
        boolean x = rdbCHV1.isSelected();
        if(x)
        {
            jTabbedPane.remove(pnlUnblock);
            jTabbedPane.addTab("Disable", pnlDisable);
            jTabbedPane.addTab("Enable", pnlEnable);
            jTabbedPane.addTab("Unblock", pnlUnblock);
        }
        else
        {
            jTabbedPane.remove(pnlDisable);
            jTabbedPane.remove(pnlEnable);
        }
        jTabbedPane.setSelectedIndex(0);
        
        if(rdbCHV1.isSelected())
        {
            //Verify
            edtVerifyCHV.setEnabled(dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            btnVerifyCHV.setEnabled(dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            //Change
            edtOldCHV.setEnabled(dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            edtNewCHVChange.setEnabled(dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            btnChangeCHV.setEnabled(dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            //Disable
            edtDisableCHV.setEnabled(!dfMaster.isCHV1Blocked() && dfMaster.isCHV1Enabled());
            btnDisableCHV.setEnabled(!dfMaster.isCHV1Blocked() && dfMaster.isCHV1Enabled());
            //Enable
            edtEnableCHV.setEnabled(!dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            btnEnableCHV.setEnabled(!dfMaster.isCHV1Enabled() && !dfMaster.isCHV1Blocked());
            //Unblock
            //always enabled
            
        }
        else
        {
            //Verify
            edtVerifyCHV.setEnabled(!dfMaster.isCHV2Blocked());
            btnVerifyCHV.setEnabled(!dfMaster.isCHV2Blocked());
            //Change
            edtOldCHV.setEnabled(!dfMaster.isCHV2Blocked());
            edtNewCHVChange.setEnabled(!dfMaster.isCHV2Blocked());
            btnChangeCHV.setEnabled(!dfMaster.isCHV2Blocked());
            //Unblock
            //always enabled
            
        }
        
        txtArea.setText(dfMaster.getCHVInfo());
    }
}
