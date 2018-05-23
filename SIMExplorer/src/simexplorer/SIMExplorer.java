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
import simexplorer.files.File;
import simexplorer.files.EF;
import simexplorer.files.DF;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import simexplorer.reportgenerator.ReportGenerator;
import java.util.List;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import simexplorer.decoders.DecodeSMS;
import simexplorer.decoders.SIMFileNotFoundException;
import simexplorer.simcardcloner.SIMCardType;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class SIMExplorer extends javax.swing.JFrame implements APDUSender {
    //"Fabrica" de Terminais PC/SC  
    TerminalFactory factory;  
    //Lista de Leitores PC/SC  
    List terminals;  
    //Terminal PC/SC  
    CardTerminal terminal;  

    //Smart Card  
    Card card;  
    //Smart Card ATR  
    ATR cardATR;  
    //Canal de Comunicação com o Smart Card  
    CardChannel cardChannel;  
    //APDU de Comando  
    CommandAPDU commandAPDU;  
    //APDU de Resposta  
    ResponseAPDU responseAPDU;  
    //Buffer de Auxilio  
    
    private Thread threadWaitForCardAbsent = null;
    
    private File lastFile = null;
    
    private SIMCardType simCardType = SIMCardType.Regular;
    private javax.swing.tree.DefaultMutableTreeNode treeDF_ADMIN = null;    
    
    
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    
    
    
    /**
     * Creates new form SIMExplorer
     */
    public SIMExplorer() {
        initComponents();
        
        DecodeSMS.setShowTimeZone(chkMostraFuso.isSelected());
        
        desconectando();

        lstHistoria.setModel(listModel);

        //Adquire Fabrica de Leitores  
        factory = TerminalFactory.getDefault();         
        
        try {
            //Adquire Lista de Leitores PC/SC no Sistema
            terminals = factory.terminals().list();
        } catch (CardException ex) {
            JOptionPane.showMessageDialog(null, ex, null, 0);
        }
        System.out.println("List : " + terminals);  

        for(int i=0;i<terminals.size();i++)
        {
            terminal = (CardTerminal)terminals.get(i);  
            MenuItemTerminal mnuItemConectar = new MenuItemTerminal(terminal.getName());
            mnuItemConectar.terminal_number = i;
                   
            
            mnuItemConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemConectarActionPerformed(evt);
            }
            
            });
            
            mnuConectar.add(mnuItemConectar);    
        }
    }

    private static String formatBuffer(byte[] buffer)  
    {  
        StringBuilder strBuff = new StringBuilder("");  
        for (int i = 0; i < buffer.length; i++) {  
            strBuff.append(String.format("%02X ", buffer[i]));  
        }  
        return strBuff.toString();  
    }  
    
    @Override
    public byte[] enviarAPDU(byte[] c)
    {
        commandAPDU = new CommandAPDU( c );
        adicionarHistoria("S:" + formatBuffer(c));
        try {  
            responseAPDU = cardChannel.transmit(commandAPDU);
        } catch (CardException ex) {
            adicionarHistoria("Err: " + ex.getMessage());
        }
        adicionarHistoria("R:" + formatBuffer(responseAPDU.getBytes()));
        return responseAPDU.getBytes();
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuTreeFiles = new javax.swing.JPopupMenu();
        mnuEditar = new javax.swing.JMenuItem();
        mnuLstHistoria = new javax.swing.JPopupMenu();
        mnuLstHistoriaCopiar = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeFiles = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        edtDadosBrutos = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        edtDadosDecodificados = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstHistoria = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        mnuSIMCard = new javax.swing.JMenu();
        mnuConectar = new javax.swing.JMenu();
        mnuDesconectar = new javax.swing.JMenuItem();
        mnuCriarRelatorio = new javax.swing.JMenuItem();
        mnuEnviarAPDU = new javax.swing.JMenuItem();
        mnuGerenciarCHV = new javax.swing.JMenuItem();
        mnuCopia = new javax.swing.JMenu();
        mnuSIMCardParaArquivo = new javax.swing.JMenuItem();
        mnuArquivoParaSimCard = new javax.swing.JMenuItem();
        mnuCodificacao = new javax.swing.JMenu();
        mnuSMS = new javax.swing.JMenu();
        chkMostraFuso = new javax.swing.JCheckBoxMenuItem();
        mnuAjuda = new javax.swing.JMenu();
        mnuSobre = new javax.swing.JMenuItem();

        mnuTreeFiles.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                mnuTreeFilesPopupMenuWillBecomeVisible(evt);
            }
        });

        mnuEditar.setText("jMenuItem1");
        mnuEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEditarActionPerformed(evt);
            }
        });
        mnuTreeFiles.add(mnuEditar);

        mnuLstHistoriaCopiar.setText("Copiar");
        mnuLstHistoriaCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLstHistoriaCopiarActionPerformed(evt);
            }
        });
        mnuLstHistoria.add(mnuLstHistoriaCopiar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIM Explorer");
        setLocationByPlatform(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("MF");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ICCID");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("TELECOM");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ADN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("FDN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SMS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CCP");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("MSISDN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SMSP");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SMSS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("LND");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SDN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("EXT1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("EXT2");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("EXT3");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("GSM");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("LP");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("IMSI");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("KC");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PLMNSel");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("HPLMN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ACMmax");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SST");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ACM");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("GID1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("GID2");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PUCT");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CBMI");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SPN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CBMID");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("BCCH");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ACC");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("FPLMN");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("LOCI");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("AD");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("PHASE");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("VGCS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("VGCSS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("VBS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("VBSS");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("eMLPP");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("AAeM");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ECC");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("CBMIR");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeFiles.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeFiles.setComponentPopupMenu(mnuTreeFiles);
        treeFiles.setEnabled(false);
        treeFiles.setRootVisible(false);
        treeFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                treeFilesMousePressed(evt);
            }
        });
        treeFiles.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeFilesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeFiles);

        edtDadosBrutos.setEditable(false);
        edtDadosBrutos.setColumns(20);
        edtDadosBrutos.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        edtDadosBrutos.setRows(5);
        jScrollPane3.setViewportView(edtDadosBrutos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Raw data", jPanel1);

        edtDadosDecodificados.setEditable(false);
        edtDadosDecodificados.setColumns(20);
        edtDadosDecodificados.setRows(5);
        jScrollPane4.setViewportView(edtDadosDecodificados);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Decoded data", jPanel2);

        lstHistoria.setComponentPopupMenu(mnuLstHistoria);
        jScrollPane2.setViewportView(lstHistoria);

        mnuSIMCard.setText("SIM Card");

        mnuConectar.setText("Connect");
        mnuSIMCard.add(mnuConectar);

        mnuDesconectar.setText("Disconnect");
        mnuDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDesconectarActionPerformed(evt);
            }
        });
        mnuSIMCard.add(mnuDesconectar);

        mnuCriarRelatorio.setText("Generate UFED like report...");
        mnuCriarRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCriarRelatorioActionPerformed(evt);
            }
        });
        mnuSIMCard.add(mnuCriarRelatorio);

        mnuEnviarAPDU.setText("Send APDU...");
        mnuEnviarAPDU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEnviarAPDUActionPerformed(evt);
            }
        });
        mnuSIMCard.add(mnuEnviarAPDU);

        mnuGerenciarCHV.setText("Manage CHV...");
        mnuGerenciarCHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGerenciarCHVActionPerformed(evt);
            }
        });
        mnuSIMCard.add(mnuGerenciarCHV);

        mnuCopia.setText("Copy");

        mnuSIMCardParaArquivo.setText("SIM Card to file...");
        mnuSIMCardParaArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSIMCardParaArquivoActionPerformed(evt);
            }
        });
        mnuCopia.add(mnuSIMCardParaArquivo);

        mnuArquivoParaSimCard.setText("File to SIM Card...");
        mnuArquivoParaSimCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuArquivoParaSimCardActionPerformed(evt);
            }
        });
        mnuCopia.add(mnuArquivoParaSimCard);

        mnuSIMCard.add(mnuCopia);

        menuBar.add(mnuSIMCard);

        mnuCodificacao.setText("Decode");

        mnuSMS.setText("SMS");

        chkMostraFuso.setSelected(true);
        chkMostraFuso.setText("Show time zone");
        chkMostraFuso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMostraFusoActionPerformed(evt);
            }
        });
        mnuSMS.add(chkMostraFuso);

        mnuCodificacao.add(mnuSMS);

        menuBar.add(mnuCodificacao);

        mnuAjuda.setText("Help");

        mnuSobre.setText("About...");
        mnuSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSobreActionPerformed(evt);
            }
        });
        mnuAjuda.add(mnuSobre);

        menuBar.add(mnuAjuda);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTabbedPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeFilesValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeFilesValueChanged

        edtDadosBrutos.setText("");
        edtDadosDecodificados.setText("");

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeFiles.getLastSelectedPathComponent(); 
        if (node == null)
            return;

        if(node.getChildCount() == 0)
        {
            //EF
            String pais[] = new String[treeFiles.isRootVisible()?node.getLevel():node.getLevel()-1];
            for(int i=0; i<pais.length;i++)
            {
                pais[i] = node.getPath()[treeFiles.isRootVisible()?i:i+1].toString();
            }

            atualizarEdtEF(node.toString(), pais);

        }
        else
        {
            //DF

            String pais[] = new String[treeFiles.isRootVisible()?node.getLevel():node.getLevel()-1];
            for(int i=0; i<pais.length;i++)
            {
                pais[i] = node.getPath()[treeFiles.isRootVisible()?i:i+1].toString();
            }

            atualizarEdtDF(node.toString(), pais);


        }
        edtDadosBrutos.setSelectionStart(0);
        edtDadosBrutos.setSelectionEnd(0);
        edtDadosDecodificados.setSelectionStart(0);
        edtDadosDecodificados.setSelectionEnd(0);
    
        lstHistoria.ensureIndexIsVisible(lstHistoria.getModel().getSize()-1);
        
    }//GEN-LAST:event_treeFilesValueChanged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

    }//GEN-LAST:event_formMousePressed

    private void treeFilesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeFilesMousePressed
    }//GEN-LAST:event_treeFilesMousePressed

    private void mnuTreeFilesPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_mnuTreeFilesPopupMenuWillBecomeVisible
        
        Object lastPath = null;
        if(treeFiles.getSelectionPath() != null) 
            lastPath = treeFiles.getSelectionPath().getLastPathComponent();
        if(lastPath != null) 
        {
            mnuEditar.setEnabled(true);
            mnuEditar.setText("Edit " + lastPath  + "...");
        }
        else
        {
            mnuEditar.setEnabled(false);
            mnuEditar.setText("Edit...");
        }
        
    }//GEN-LAST:event_mnuTreeFilesPopupMenuWillBecomeVisible

    private void mnuEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEditarActionPerformed
        if(lastFile.getClass() == EF.class )
        {
            EF ef = (EF)lastFile;
            if(ef.getStructureOfFile() == EF.StructureOfFile.Transparent)
            {
                DialogEditar dlgEditar = new DialogEditar(this, true);
                dlgEditar.txtEdicao.setText(ef.getContentsTransparentAsString());
                dlgEditar.setTitle("Edit " + ef.getNome());
                dlgEditar.setVisible(true);
                if(dlgEditar.getPressionouOK())
                {
                    try                    
                    {
                        ef.updateBinary(dlgEditar.txtEdicao.getText());
                        atualizarEdtEF(lastFile.getNome(), lastFile.getPais());
                    }
                    catch(EF.UpdateBinaryException ex)
                    {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                        
                }
            }
            else if(ef.getStructureOfFile() == EF.StructureOfFile.LinearFixed)
            {
                
                DialogNumReg dlgNumReg = new DialogNumReg(this, true, ef.getNumRegs());
                dlgNumReg.setVisible(true);
                if(dlgNumReg.getPressionouOK())
                {
                    DialogEditar dlgEditar = new DialogEditar(this, true);
                    dlgEditar.txtEdicao.setText(ef.getContentsLinearCyclicAsString(dlgNumReg.getNumReg()-1));
                    dlgEditar.setTitle("Edit " + ef.getNome() + "; register " + dlgNumReg.getNumReg());
                    dlgEditar.setVisible(true);
                    if(dlgEditar.getPressionouOK())
                    {
                    try                    
                    {
                        ef.updateRecord(dlgEditar.txtEdicao.getText(), dlgNumReg.getNumReg());
                        atualizarEdtEF(lastFile.getNome(), lastFile.getPais());
                    }
                    catch(EF.UpdateRecordException ex)
                    {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    }
                            
                
                    
                }
                
            }
        
            else if(ef.getStructureOfFile() == EF.StructureOfFile.Cyclic )
            {
                DialogEditar dlgEditar = new DialogEditar(this, true);
                dlgEditar.txtEdicao.setText(ef.getContentsLinearCyclicAsString(0));
                dlgEditar.setTitle("New register: " + ef.getNome());
                dlgEditar.setVisible(true);
                if(dlgEditar.getPressionouOK())
                {
                    try                    
                    {
                        ef.updateRecord(dlgEditar.txtEdicao.getText(), 0);
                        atualizarEdtEF(lastFile.getNome(), lastFile.getPais());
                    }
                    catch(EF.UpdateRecordException ex)
                    {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                        
                }
            
            
            }
        }
    }//GEN-LAST:event_mnuEditarActionPerformed

    private void mnuCriarRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCriarRelatorioActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("report.xml"));
        
        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            if(fc.getSelectedFile().exists())
            {
                JOptionPane.showMessageDialog(null, "File already exists.");
                return;
            }
            try
            {
                ReportGenerator.generate(fc.getSelectedFile(), this);
                JOptionPane.showMessageDialog(null, "File created.");
            }
            catch(SIMFileNotFoundException | ParserConfigurationException | TransformerException ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            
        }
    }//GEN-LAST:event_mnuCriarRelatorioActionPerformed

    private void mnuDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDesconectarActionPerformed
        
        try {
            adicionarHistoria("Disconnected");
            card.disconnect(true);
            if (terminal.isCardPresent()) desconectando();
        } catch (CardException ex) {
            
        }
        
        
        
        
    }//GEN-LAST:event_mnuDesconectarActionPerformed

    private void mnuLstHistoriaCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLstHistoriaCopiarActionPerformed
        
        if(lstHistoria.getSelectedValue()!=null)
        {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(lstHistoria.getSelectedValue().toString());
            cb.setContents(ss, null);
        }
        
    }//GEN-LAST:event_mnuLstHistoriaCopiarActionPerformed

    private void mnuEnviarAPDUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEnviarAPDUActionPerformed
        (new DialogEnviarAPDU(this, rootPaneCheckingEnabled, this)).setVisible(true);
    }//GEN-LAST:event_mnuEnviarAPDUActionPerformed

    private void mnuGerenciarCHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGerenciarCHVActionPerformed
        
        (new DialogCHV(this, true, this)).setVisible(true);
        
    }//GEN-LAST:event_mnuGerenciarCHVActionPerformed

    private void mnuSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSobreActionPerformed
        (new DialogSobre(this, true)).setVisible(true);
    }//GEN-LAST:event_mnuSobreActionPerformed

    private void mnuSIMCardParaArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSIMCardParaArquivoActionPerformed
        
        
        (new Thread(new Runnable() {

            public void run() {
        
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(SIMExplorer.this);        


                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    if(fc.getSelectedFile().exists())
                    {
                        int r = JOptionPane.showConfirmDialog(null, "File already exists. Replace?", "", JOptionPane.YES_NO_OPTION);
                        if(r==JOptionPane.OK_OPTION)
                        {
                            if(fc.getSelectedFile().delete())
                            {
                                DialogCopia dialog = new DialogCopia(null, fc.getSelectedFile(), SIMExplorer.this, true, SIMExplorer.this.simCardType);
                                dialog.setVisible(true);
                            }    
                            else
                                JOptionPane.showMessageDialog(null, "Can not delete file.");
                        }

                    }
                    else
                    {
                        //Abrir dialogo
                        DialogCopia dialog = new DialogCopia(null, fc.getSelectedFile(), SIMExplorer.this, true, SIMExplorer.this.simCardType);
                        dialog.setVisible(true);
                        
                    }
                }
            }
        })).start();

        
    }//GEN-LAST:event_mnuSIMCardParaArquivoActionPerformed

    private void mnuArquivoParaSimCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArquivoParaSimCardActionPerformed

        
        (new Thread(new Runnable() {

            public void run() {
        
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(SIMExplorer.this);        


                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    if(fc.getSelectedFile().exists())
                    {
                        DialogCopia dialog = new DialogCopia(null, fc.getSelectedFile(), SIMExplorer.this, false, SIMExplorer.this.simCardType);
                        dialog.setVisible(true);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "File doesn't exist.");
                    }
                }
            }
        })).start();
        

    }//GEN-LAST:event_mnuArquivoParaSimCardActionPerformed

    private void chkMostraFusoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMostraFusoActionPerformed
        DecodeSMS.setShowTimeZone(chkMostraFuso.isSelected());
    }//GEN-LAST:event_chkMostraFusoActionPerformed

    private void conectando()
    {
        treeFiles.setEnabled(true);
        mnuConectar.setVisible(false);
        mnuDesconectar.setVisible(true);
        mnuCriarRelatorio.setEnabled(true);
        mnuEnviarAPDU.setEnabled(true);
        mnuGerenciarCHV.setEnabled(true);
        mnuCopia.setEnabled(true);
        
        this.simCardType = detectSIMCardType();
        if(this.simCardType == SIMCardType.MagicSIM)
        {
            adicionarMagicSimFiles();
            JOptionPane.showMessageDialog(null, "MagicSim detected!");
        }

    }
    
    private void desconectando()
    {
        mnuDesconectar.setVisible(false);
        mnuConectar.setVisible(true);
        treeFiles.setEnabled(false);
        mnuCriarRelatorio.setEnabled(false);
        edtDadosBrutos.setText("");
        edtDadosDecodificados.setText("");
        treeFiles.setSelectionPath(null);
        mnuEnviarAPDU.setEnabled(false);
        mnuGerenciarCHV.setEnabled(false);
        mnuCopia.setEnabled(false);
        
        if(this.simCardType == SIMCardType.MagicSIM)
        {
            removerMagicSimFiles();
            
        }
            
        
    }
    
    private void mnuItemConectarActionPerformed(java.awt.event.ActionEvent evt) {                                                

        MenuItemTerminal mnuItemConectar = (MenuItemTerminal) evt.getSource();

        try {
            //Estabelece Conexão com o Cartão na Leitora
            terminal = (CardTerminal) terminals.get(mnuItemConectar.terminal_number);
            adicionarHistoria("Selected terminal: " + terminal.getName());
            card =  terminal.connect("T=0");
            adicionarHistoria("Connected");
        } catch (CardException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Err", 0, null);
            return;
        }
        System.out.println("card: " + card);

        //Adquire ATR do Cartão
        cardATR = card.getATR();
        byte[] buffer = cardATR.getBytes();
        adicionarHistoria("ATR : "  + formatBuffer(buffer));

        //Adquire Canal de Comunicação
        cardChannel = card.getBasicChannel();
        
        
        
        conectando();

        if(threadWaitForCardAbsent == null)
        {
            threadWaitForCardAbsent = (new Thread(new Runnable() {
                
                @Override
                public synchronized void  run() {
                    while(true)
                    {
                        try {
                    
                            terminal.waitForCardAbsent(0);
                            if(mnuDesconectar.isVisible()) adicionarHistoria("Disconnected");
                            desconectando();
                            terminal.waitForCardPresent(0);
                        
                        } catch (CardException ex) {
                    
                        }
                    }
                }
            }));
            threadWaitForCardAbsent.start();
        }
        
    }                                               


    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SIMExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SIMExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SIMExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SIMExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>




        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SIMExplorer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem chkMostraFuso;
    private javax.swing.JTextArea edtDadosBrutos;
    private javax.swing.JTextArea edtDadosDecodificados;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList lstHistoria;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnuAjuda;
    private javax.swing.JMenuItem mnuArquivoParaSimCard;
    private javax.swing.JMenu mnuCodificacao;
    private javax.swing.JMenu mnuConectar;
    private javax.swing.JMenu mnuCopia;
    private javax.swing.JMenuItem mnuCriarRelatorio;
    private javax.swing.JMenuItem mnuDesconectar;
    private javax.swing.JMenuItem mnuEditar;
    private javax.swing.JMenuItem mnuEnviarAPDU;
    private javax.swing.JMenuItem mnuGerenciarCHV;
    private javax.swing.JPopupMenu mnuLstHistoria;
    private javax.swing.JMenuItem mnuLstHistoriaCopiar;
    private javax.swing.JMenu mnuSIMCard;
    private javax.swing.JMenuItem mnuSIMCardParaArquivo;
    private javax.swing.JMenu mnuSMS;
    private javax.swing.JMenuItem mnuSobre;
    private javax.swing.JPopupMenu mnuTreeFiles;
    private javax.swing.JTree treeFiles;
    // End of variables declaration//GEN-END:variables

    private void adicionarHistoria(String txtTexto)
    {
        listModel.addElement(txtTexto);
    }

    private void atualizarEdtEF(String nome, String[] pais) {
        try
        {
            EF ef = new EF(this, nome, pais);
            lastFile = ef;
            edtDadosBrutos.setText(ef.toString());
            edtDadosDecodificados.setText(ef.dadosDecodificados());
        }
        catch(SIMFileNotFoundException ex)
        {
            edtDadosBrutos.setText(ex.getMessage());
            edtDadosDecodificados.setText(ex.getMessage());
            lastFile = null;
        }
        finally
        {

            edtDadosBrutos.setSelectionStart(0);
            edtDadosBrutos.setSelectionEnd(0);
            edtDadosDecodificados.setSelectionStart(0);
            edtDadosDecodificados.setSelectionEnd(0);
        }
        
    }

    private void atualizarEdtDF(String nome, String[] pais) {
        try
        {
            DF df = new DF(this, nome, pais);
            lastFile = df;
            edtDadosBrutos.setText(df.toString());
        }
        catch(SIMFileNotFoundException ex)
        {
            edtDadosBrutos.setText(ex.getMessage());
            lastFile = null;


        }
        finally
        {

            edtDadosBrutos.setSelectionStart(0);
            edtDadosBrutos.setSelectionEnd(0);
            edtDadosDecodificados.setSelectionStart(0);
            edtDadosDecodificados.setSelectionEnd(0);
        }
            
            
    }

    private SIMCardType detectSIMCardType() {
        byte response[] = this.enviarAPDU(new byte[]{(byte)0xa0, (byte)0xa4, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x7f, (byte)0x4d});
        if(response.length == 2)
        {
            if(response[0] == (byte)0x9f)
                return SIMCardType.MagicSIM;
            else
                return SIMCardType.Regular;
        }
        else
            return SIMCardType.Regular;
    }

    private void adicionarMagicSimFiles() {

        javax.swing.tree.DefaultMutableTreeNode treeRoot;
        javax.swing.tree.DefaultMutableTreeNode treeNode3;
        treeDF_ADMIN = new javax.swing.tree.DefaultMutableTreeNode("DF.ADMIN");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("EF.OPN");
        treeDF_ADMIN.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("EF 8f 0d");
        treeDF_ADMIN.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("EF 8f 0e");
        treeDF_ADMIN.add(treeNode3);
        treeRoot = ((javax.swing.tree.DefaultMutableTreeNode) treeFiles.getModel().getRoot());
        treeRoot.add(treeDF_ADMIN);
        treeFiles.setModel(new javax.swing.tree.DefaultTreeModel(treeRoot));    
    
    }

    private void removerMagicSimFiles() {
        javax.swing.tree.DefaultMutableTreeNode treeRoot = ((javax.swing.tree.DefaultMutableTreeNode) treeFiles.getModel().getRoot());
        try
        {
            treeRoot.remove(treeDF_ADMIN);
        }
        catch(Exception e)
        {
                    
        }
        treeFiles.setModel(new javax.swing.tree.DefaultTreeModel(treeRoot));    

    }


    private class MenuItemTerminal extends JMenuItem
    {
        int terminal_number;

        private MenuItemTerminal(String name) {
            super(name);
        }
    }
       
}
