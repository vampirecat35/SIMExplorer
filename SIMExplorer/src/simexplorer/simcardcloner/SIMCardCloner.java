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

package simexplorer.simcardcloner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import simexplorer.apdusender.APDUSender;
import simexplorer.decoders.SIMFileNotFoundException;
import simexplorer.files.EF;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class SIMCardCloner {


    
    public void fileToSIMCard(File file, SIMCardClonerListener l, APDUSender apduSender, SIMCardType simCardType) 
    {
            Thread t = (new Thread(new Runnable() {

                @Override
                public void run() {
                    if(l!=null)l.simCardClonerInit();
                
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    Document doc;
                    DocumentBuilder dBuilder;
                    try {
                        dBuilder = dbFactory.newDocumentBuilder();
                        doc = dBuilder.parse(file);
                    } catch (Exception ex) {
                        if(l!=null)l.simCardClonerEvent(ex.toString());
                        if(l!=null)l.simCardClonerEnd(false);
                        return;
                    }
                    doc.getDocumentElement().normalize();
                    
                    Element root = doc.getDocumentElement();
                    if(!root.getNodeName().equals("MF"))
                    {
                        if(l!=null)l.simCardClonerEvent("Invalid file.");
                        if(l!=null)l.simCardClonerEnd(false);
                        return;
                        
                    }
                        
                    //MF
                    for(int i=0; i<mfMap.length;i++)
                    {
                        NodeList nList = root.getElementsByTagName(mfMap[i]);
                        if(nList.getLength() > 0)
                        {
                            Element elementFile = (Element)nList.item(0); //use the first one only
                            try {
                                elementFileToSIMCard(elementFile, mfMap[i], new String[]{"MF"}, l, apduSender);
                            } catch (SIMFileNotFoundException ex) {
                                if(l!=null)l.simCardClonerEvent("   EF not found in SIM Card: " + ex.toString());
                            } catch (EF.UpdateBinaryException | EF.UpdateRecordException ex) {
                                if(l!=null)l.simCardClonerEvent("   Can't update file: " + ex.toString());
                            }
                            
                        }
                    }
                        
                    //TELECOM
                    NodeList nList = root.getElementsByTagName("TELECOM");
                    Element telecom = (Element)nList.item(0); //use the first one only
                    for(int i=0; i<telecomMap.length;i++)
                    {
                        nList = telecom.getElementsByTagName(telecomMap[i]);
                        if(nList.getLength() > 0)
                        {
                            Element elementFile = (Element)nList.item(0); //use the first one only
                            try {
                                elementFileToSIMCard(elementFile, telecomMap[i], new String[]{"MF", "TELECOM"}, l, apduSender);
                            } catch (SIMFileNotFoundException ex) {
                                if(l!=null)l.simCardClonerEvent("   EF " + " not found in SIM Card: " + ex.toString());
                            } catch (EF.UpdateBinaryException | EF.UpdateRecordException ex) {
                                if(l!=null)l.simCardClonerEvent("   Can't update file: " + ex.toString());
                            }

                        }
                    }

                    //GSM
                    nList = root.getElementsByTagName("GSM");
                    Element gsm = (Element)nList.item(0); //use the first one only
                    for(int i=0; i<gsmMap.length;i++)
                    {
                        nList = gsm.getElementsByTagName(gsmMap[i]);
                        if(nList.getLength() > 0)
                        {
                            Element elementFile = (Element)nList.item(0); //use the first one only
                            try {
                                elementFileToSIMCard(elementFile, gsmMap[i], new String[]{"MF", "GSM"}, l, apduSender);
                            } catch (SIMFileNotFoundException ex) {
                                if(l!=null)l.simCardClonerEvent("   EF " + gsmMap[i]  + " not found in SIM Card: " + ex.toString());
                            } catch (EF.UpdateBinaryException | EF.UpdateRecordException ex) {
                                if(l!=null)l.simCardClonerEvent("   Can't update file: " + ex.toString());
                            }

                        }
                    }
                    
                    //MAGIC SIM!!!
                    if(simCardType == SIMCardType.MagicSIM)
                    {
                        //ICCID
                        if(l!=null)l.simCardClonerEvent("Attempt to change ICCID throw MagicSim special files...");
                        try {
                            EF ef_EF_8f_0d = new EF(apduSender, "EF 8f 0d", new String[]{"MF", "DF.ADMIN"});
                            nList = root.getElementsByTagName("ICCID");
                            Element contents = (Element)((Element)(nList.item(0))).getElementsByTagName("Contents").item(0);
                            String str = contents.getFirstChild().getNodeValue();
                            byte[] iccid_fromfile = getBinaryContents(str, 10, "ICCID", l );
                            
                            byte[] ef_EF_8f_0d_contents = ef_EF_8f_0d.getContentsLinearCyclic(0);
                            System.arraycopy(iccid_fromfile, 0, ef_EF_8f_0d_contents, 21, 10);
                            ef_EF_8f_0d.updateRecord(ef_EF_8f_0d_contents, 1);
                            //compare now
                            EF ef_ICCID = new EF(apduSender, "ICCID", new String[]{"MF"});
                            byte[] iccid_fromsim = ef_ICCID.getContentsTransparent();
                            if(Arrays.equals(iccid_fromsim, iccid_fromfile))
                            {
                                if(l!=null)l.simCardClonerEvent("   Updated!");
                            }
                            else
                            {
                                if(l!=null)l.simCardClonerEvent("   Falha!");
                            }
                                
                            
                        } catch (SIMFileNotFoundException | EF.UpdateRecordException ex ) {
                            if(l!=null)l.simCardClonerEvent("   Falha: " + ex.toString());
                        }    
                            
                        //IMSI
                        if(l!=null)l.simCardClonerEvent("Attempt to change IMSI throw MagicSim special files...");
                        try {
                            EF ef_EF_8f_0d = new EF(apduSender, "EF 8f 0d", new String[]{"MF", "DF.ADMIN"});
                            nList = root.getElementsByTagName("IMSI");
                            Element contents = (Element)((Element)(nList.item(0))).getElementsByTagName("Contents").item(0);
                            if(contents.getFirstChild() == null)
                            {
                                if(l!=null)l.simCardClonerEvent("   Falha!");
                            }
                            else
                            {
                                String str = contents.getFirstChild().getNodeValue();
                                byte[] imsi_fromfile = getBinaryContents(str, 9, "IMSI", l );

                                byte[] ef_EF_8f_0d_contents = ef_EF_8f_0d.getContentsLinearCyclic(0);
                                System.arraycopy(imsi_fromfile, 0, ef_EF_8f_0d_contents, 36, 9);
                                ef_EF_8f_0d.updateRecord(ef_EF_8f_0d_contents, 1);
                                //compare now
                                EF ef_IMSI = new EF(apduSender, "IMSI", new String[]{"MF", "GSM"});
                                byte[] imsi_fromsim = ef_IMSI.getContentsTransparent();
                                if(Arrays.equals(imsi_fromsim, imsi_fromfile))
                                {
                                    if(l!=null)l.simCardClonerEvent("   Updated!");
                                }
                                else
                                {
                                    if(l!=null)l.simCardClonerEvent("   Fail!");
                                }
                            }
                                
                            
                        } catch (SIMFileNotFoundException | EF.UpdateRecordException ex ) {
                            if(l!=null)l.simCardClonerEvent("   Fail: " + ex.toString());
                        }    
                        
                        
                        //SPN
                        if(l!=null)l.simCardClonerEvent("Attempt to change SPN throw MagicSim special files...");
                        try {
                            EF ef_OPN = new EF(apduSender, "EF.OPN", new String[]{"MF", "DF.ADMIN"});
                            nList = root.getElementsByTagName("SPN");
                            Element contents = (Element)((Element)(nList.item(0))).getElementsByTagName("Contents").item(0);
                            if(contents.getFirstChild() == null)
                            {
                                if(l!=null)l.simCardClonerEvent("   Fail!");
                            }    
                            else  
                            {
                                String str = contents.getFirstChild().getNodeValue();
                                byte[] spn_fromfile = getBinaryContents(str, 17, "SPN", l );

                                byte[] ef_OPN_contents = ef_OPN.getContentsLinearCyclic(1);
                                System.arraycopy(spn_fromfile, 1, ef_OPN_contents, 0, 14);
                                ef_OPN.updateRecord(ef_OPN_contents, 2);
                                //compare now
                                EF ef_SPN = new EF(apduSender, "SPN", new String[]{"MF", "GSM"});
                                byte[] spn_fromsim = ef_SPN.getContentsTransparent();
                                boolean equal = true;
                                for(int i=1;i<=14;i++)
                                    equal &= (spn_fromsim[i] == spn_fromfile[i]);
                                if(equal)
                                {
                                    if(l!=null)l.simCardClonerEvent("   Updated!");
                                }
                                else
                                {
                                    if(l!=null)l.simCardClonerEvent("   Fail!");
                                }
                            }
                                
                            
                        } catch (SIMFileNotFoundException | EF.UpdateRecordException ex ) {
                            if(l!=null)l.simCardClonerEvent("   Fail: " + ex.toString());
                        }    
                        
                        
                        
                    }

                    if(l!=null)l.simCardClonerEnd(true);
                
                    
                    
            }}));
            
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    if(l!=null)l.simCardClonerEvent("Error: " + e.toString());
                    if(l!=null)l.simCardClonerEnd(false);
                }
            });
            
            t.start();
            
    
        
    }
    
    private void elementFileToSIMCard(Element elementFile, String ef_name, String[] pais, SIMCardClonerListener l, APDUSender apduSender) throws SIMFileNotFoundException, EF.UpdateBinaryException, EF.UpdateRecordException
    {
        NodeList nList = elementFile.getElementsByTagName("StructureOfFile");
        Element structureOfFile = (Element)nList.item(0);
        if(l!=null)l.simCardClonerEvent("Copying " + ef_name + "...");
        if(structureOfFile.getFirstChild().getNodeValue().equals("Transparent"))
        {
            if(l!=null)l.simCardClonerEvent("  Structure of file:  Transparent");
            nList = elementFile.getElementsByTagName("FileSize");
            int fileSize = Integer.parseInt(((Element)nList.item(0)).getFirstChild().getNodeValue());
            if(l!=null)l.simCardClonerEvent("  File size:  " + fileSize);

            
            nList = elementFile.getElementsByTagName("Contents");
            Element contents = (Element)nList.item(0);
            EF ef = new EF(apduSender, ef_name, pais);
            byte[] binaryContents;
            if(contents.getFirstChild()==null)
                binaryContents = new byte[ef.getFileSize()];
            else
                binaryContents = getBinaryContents(contents.getFirstChild().getNodeValue(), ef.getFileSize(), ef_name, l);
            if(l!=null)l.simCardClonerEvent("  Contents:  " + strBinaryContents(binaryContents));
            ef.updateBinary(binaryContents);
            if(l!=null)l.simCardClonerEvent("  Updated!");
            
        }
        else if(structureOfFile.getFirstChild().getNodeValue().equals("Linear Fixed"))
        {
            if(l!=null)l.simCardClonerEvent("  Structure of file:  Linear Fixed");
            nList = elementFile.getElementsByTagName("FileSize");
            int fileSize = Integer.parseInt(((Element)nList.item(0)).getFirstChild().getNodeValue());
            if(l!=null)l.simCardClonerEvent("  File size:  " + fileSize);
            nList = elementFile.getElementsByTagName("NumRegs");
            int numRegs = Integer.parseInt(((Element)nList.item(0)).getFirstChild().getNodeValue());
            if(l!=null)l.simCardClonerEvent("  Num Regs:  " + numRegs);
            
            nList = elementFile.getElementsByTagName("Contents");
            Element contents = (Element)nList.item(0);
            EF ef = new EF(apduSender, ef_name, pais);
        
            if(numRegs > ef.getNumRegs())
                if(l!=null)l.simCardClonerEvent("   Warning: number of records in SIM Card " + ef_name+ " file is smaller than in local file. Some data may not be copied.");    
            for(int i=0;i<ef.getNumRegs();i++)
            {
                if(i<numRegs)
                {
                    nList = contents.getElementsByTagName("Reg" + i);
                    Element reg = (Element)nList.item(0);
                    byte[] binaryContents = getBinaryContents(reg.getFirstChild().getNodeValue(), ef.getFileSize()/ef.getNumRegs() , ef_name, l);
                    if(l!=null)l.simCardClonerEvent("   Reg" + i + " = " +  strBinaryContents(binaryContents));
                    ef.updateRecord(binaryContents, i+1);
                    if(l!=null)l.simCardClonerEvent("   Updated!");

                }
                else
                {
                    //fill FF
                    if(l!=null)l.simCardClonerEvent("   Filling Reg" + i + " with 0xFF ");
                    byte[] fill = new byte[ef.getFileSize()/ef.getNumRegs()];
                    for(int j=0;j<fill.length;j++) fill[j]=(byte)0xFF;
                    if(ef_name.toUpperCase().equals("SMS")) fill[0]=0;
                    ef.updateRecord(fill, i+1);
                    if(l!=null)l.simCardClonerEvent("   Updated!");

                }


            }
        
                
            
        }

        else if(structureOfFile.getFirstChild().getNodeValue().equals("Cyclic"))
        {
            if(l!=null)l.simCardClonerEvent("  Structure of file: Cyclic");
            nList = elementFile.getElementsByTagName("FileSize");
            int fileSize = Integer.parseInt(((Element)nList.item(0)).getFirstChild().getNodeValue());
            if(l!=null)l.simCardClonerEvent("  File size:  " + fileSize);
            nList = elementFile.getElementsByTagName("NumRegs");
            int numRegs = Integer.parseInt(((Element)nList.item(0)).getFirstChild().getNodeValue());
            if(l!=null)l.simCardClonerEvent("  Num Regs:  " + numRegs);
            
            nList = elementFile.getElementsByTagName("Contents");
            Element contents = (Element)nList.item(0);
            EF ef = new EF(apduSender, ef_name, pais);
        
            if(numRegs > ef.getNumRegs())
                if(l!=null)l.simCardClonerEvent("   Warning: number of records in SIM Card " + ef_name + " file is smaller than in local file. Some data may not be copied.");    
            
            //clean old
            byte[] fill = new byte[ef.getFileSize()/ef.getNumRegs()];
            for(int i=0;i<fill.length;i++) fill[i]=(byte)0xFF;
            for(int i=0;i<ef.getNumRegs();i++)
            {
                ef.updateRecord(fill, 0);
            }
            if(l!=null)l.simCardClonerEvent("   Old data deleted.");
            
            
            for(int i=numRegs-1;i>=0;i--)
            {
                    nList = contents.getElementsByTagName("Reg" + i);
                    Element reg = (Element)nList.item(0);
                    byte[] binaryContents = getBinaryContents(reg.getFirstChild().getNodeValue(), ef.getFileSize()/ef.getNumRegs() , ef_name, l);
                    if(l!=null)l.simCardClonerEvent("   Reg" + i + " = " +  strBinaryContents(binaryContents));
                    ef.updateRecord(binaryContents, 0);
                    if(l!=null)l.simCardClonerEvent("   Updated!");


            }
        
                
            
        }
        
    }

    private byte[] getBinaryContents(String texto, int size, String ef_name, SIMCardClonerListener l) 
    {
        
        texto = texto.trim();
        
        String[] textos_tmp =  texto.split("\\s+");
        ArrayList textos = new ArrayList();
        
        for(int i=0;i<textos_tmp.length;i++)
            if (!"".equals(textos_tmp[i].trim()))  textos.add(textos_tmp[i].trim());
        
        byte buffer[] = new byte[textos.size()];
        
        for(int i=0; i<buffer.length;i++)
            buffer[i] = (byte)(int)Integer.valueOf(String.valueOf(textos.get(i)), 16);

        if(size != buffer.length)
        {
            if(size < buffer.length && l!=null)l.simCardClonerEvent("   Warning: length of file " + ef_name + " in SIMCard is smaller than in local file. Data will be truncated.");
            byte[] newBuffer = new byte[size];
            for(int i=0; i<newBuffer.length;i++) newBuffer[i] = (byte)0xFF;
            if(ef_name.toUpperCase().equals("ADN") || 
               ef_name.toUpperCase().equals("FDN") || 
               ef_name.toUpperCase().equals("MSISDN") || 
               ef_name.toUpperCase().equals("LND") || 
               ef_name.toUpperCase().equals("SDN") )
            {
                int x = Math.min(buffer.length-14, newBuffer.length-14);
                System.arraycopy(buffer, 0, newBuffer, 0, x);
                System.arraycopy(buffer, buffer.length-14, newBuffer, newBuffer.length-14, 14);
                return newBuffer;
            }
            else
            {
                if(size > buffer.length && l!=null)l.simCardClonerEvent("   Warning: length of file " + ef_name + " in SIMCard is greater than in local file. Extras bytes will be filled with 0xFF.");
                System.arraycopy(buffer, 0, newBuffer, 0, Math.min(buffer.length, newBuffer.length));
                return newBuffer;
            }
        }
        else
        {
                
                return buffer;
             
        }
        
    }
    
    private String strBinaryContents(byte[] binaryContents)
    {
        String str = "";
        for(int i=0; i<binaryContents.length;i++)
        {
            String hex = Integer.toHexString(0xFF&binaryContents[i]);
            if(hex.length()==1) hex = "0" + hex;
            str += " " + hex.toUpperCase();
            
        }
        return str;
    }
    
    
    public void simCardToFile(File file, SIMCardClonerListener l, APDUSender apduSender) 
    {
        
            
        
            (new Thread(new Runnable() {

                @Override
                public void run() {
                
                    if(l!=null)l.simCardClonerInit();

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuiler=null;
                    try {
                        docBuiler = dbf.newDocumentBuilder();
                    } catch (ParserConfigurationException ex) {

                    }
                    Document doc = docBuiler.newDocument();

                    //MF
                    Element elementMF = doc.createElement("MF");
                    doc.appendChild(elementMF);

                    //mfMap
                    for(int i=0; i<mfMap.length;i++)
                    {
                        Element element = createElementByMfMap(apduSender, doc,i,l);
                        elementMF.appendChild(element);
                    }

                    //TELECOM
                    Element elementTelecom = doc.createElement("TELECOM");
                    elementMF.appendChild(elementTelecom);


                    //telecomMap
                    for(int i=0; i<telecomMap.length;i++)
                    {
                        Element element = createElementByTelecomMap(apduSender, doc,i,l);
                        if(element!=null)elementTelecom.appendChild(element);
                    }

                    //GSM
                    Element elementGSM = doc.createElement("GSM");
                    elementMF.appendChild(elementGSM);

                    //gsmMap
                    for(int i=0; i<gsmMap.length;i++)
                    {
                        Element element = createElementByGsmMap(apduSender, doc,i,l);
                        if(element!=null)elementGSM.appendChild(element);
                    }                




                    // write the content into xml file
                    try {
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(file);

                        transformer.transform(source, result);
                        if(l!=null)l.simCardClonerEnd(true);
                        return;
                    } catch (TransformerException ex) {
                        if(l!=null)l.simCardClonerEnd(false);
                        return ;
                    }
                
                
                
            }})).start();
            

        
        
    }
    
    private final String[] mfMap = new String[] {
        "ICCID"
    };
    
    private final String[] telecomMap = new String[]{
        
        
        "ADN",
        "FDN",
        "SMS",
        "CCP",
        "MSISDN",
        "SMSP",
        "SMSS",
        "LND",
        "SDN",
        "EXT1",
        "EXT2",
        "EXT3",
          
        };
    
    private final String[] gsmMap = new String[]{
        
        
        "LP",
        "IMSI",
        "KC",
        "PLMNSel",
        "HPLMN",
        "ACMmax",
        "SST",
        "ACM",
        "GID1",
        "GID2",
        "PUCT",
        "CBMI",
        "SPN",
        "CBMID",
        "BCCH",
        "ACC",
        "FPLMN",
        "LOCI",
        "AD",
        "PHASE",
        "VGCS",
        "VGCSS",
        "VBS",
        "VBSS",
        "eMLPP",
        "AAeM",
        "ECC",
        "CBMIR",
          
        };

    private Element createElementByMfMap(APDUSender apduSender, Document doc, int i, SIMCardClonerListener l) {
        EF ef = null;
                    
        try { 
            if(l!=null) l.simCardClonerEvent("Copying " + mfMap[i].toString() + "...");
            ef = new EF(apduSender, mfMap[i].toString(), new String[]{"MF"});
        } catch (SIMFileNotFoundException ex) {
            if(l!=null) l.simCardClonerEvent("Not found.");
            return null;
        }
        
        return createElementByEF(ef, doc,l);
                

    }

    private Element createElementByTelecomMap(APDUSender apduSender, Document doc, int i, SIMCardClonerListener l) {
        EF ef = null;
                    
        try { 
            if(l!=null) l.simCardClonerEvent("Copying " + telecomMap[i].toString() + "...");
            ef = new EF(apduSender, telecomMap[i].toString(), new String[]{"MF", "TELECOM"});
        } catch (SIMFileNotFoundException ex) {
            if(l!=null) l.simCardClonerEvent("Not found.");
            return null;
        }
        
        return createElementByEF(ef, doc,l);
                
                
        
    }

    private Element createElementByGsmMap(APDUSender apduSender, Document doc, int i, SIMCardClonerListener l) {
        EF ef = null;
                    
        try { 
            if(l!=null) l.simCardClonerEvent("Copying " + gsmMap[i].toString() + "...");
            ef = new EF(apduSender, gsmMap[i].toString(), new String[]{"MF", "GSM"});
        } catch (SIMFileNotFoundException ex) {
            if(l!=null) l.simCardClonerEvent("Not found.");
            return null;
        }
        
        return createElementByEF(ef, doc, l);
    }
    
    private Element createElementByEF(EF ef, Document doc, SIMCardClonerListener l)
    {
        

        
        Element element = doc.createElement(ef.getNome());
        
        //FileID
        String b0 = Integer.toHexString(0xFF&ef.getFileID()[0]);
        String b1 = Integer.toHexString(0xFF&ef.getFileID()[1]);
        if(b0.length()==1) b0="0"+b0;
        if(b1.length()==1) b1="0"+b1;
        Element elementFileID = doc.createElement("FileID");
        element.appendChild(elementFileID);
        elementFileID.appendChild(doc.createTextNode(b0+b1));
        
        
        
        
        //StructureOfFile
        Element elementStructureOfFile = doc.createElement("StructureOfFile");
        element.appendChild(elementStructureOfFile);
        elementStructureOfFile.appendChild(doc.createTextNode(ef.getStructureOfFile().toString()));
        
        
        switch(ef.getStructureOfFile())
        {
            case Transparent:
            {
                //FileSize
                Element elementFileSize = doc.createElement("FileSize");
                element.appendChild(elementFileSize);
                elementFileSize.appendChild(doc.createTextNode(""+ef.getFileSize()));
                
                //Contents
                Element elementContents = doc.createElement("Contents");
                element.appendChild(elementContents);
                elementContents.appendChild(doc.createTextNode(ef.getContentsTransparentAsString()));
                
            }break;
            
            case LinearFixed:
            case Cyclic:
            {
                //FileSize
                Element elementFileSize = doc.createElement("FileSize");
                element.appendChild(elementFileSize);
                elementFileSize.appendChild(doc.createTextNode(""+ef.getFileSize()));

                //NumRegs
                Element elementNumRegs = doc.createElement("NumRegs");
                element.appendChild(elementNumRegs);
                elementNumRegs.appendChild(doc.createTextNode(""+ef.getNumRegs()));
                
                Element elementContents = doc.createElement("Contents");
                element.appendChild(elementContents);
                
                for(int i=0;i<ef.getNumRegs();i++)
                {
                    Element elementReg = doc.createElement("Reg" + i);
                    elementContents.appendChild(elementReg);
                    elementReg.appendChild(doc.createTextNode(ef.getContentsLinearCyclicAsString(i)));
                    
                }
                
                
            }break;
        }
        if(l!=null) l.simCardClonerEvent("OK!");        
        return element;
        
    }

    
    
}
