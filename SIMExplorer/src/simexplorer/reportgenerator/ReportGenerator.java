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

package simexplorer.reportgenerator;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import simexplorer.apdusender.APDUSender;
import simexplorer.files.EF;
import simexplorer.decoders.DecodeICCID;
import simexplorer.decoders.DecodeIMSI;
import simexplorer.decoders.DecodeSPN;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class ReportGenerator {

    static public void generate(File selectedFile, APDUSender apduSender) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SIMFileNotFoundException {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuiler = dbf.newDocumentBuilder();
                Document doc = docBuiler.newDocument();
                
                //Agenda
                Element reports = doc.createElement("reports");
                doc.appendChild(reports);
                
                Element report = doc.createElement("report");
                reports.appendChild(report);
                
                Element contacts = doc.createElement("contacts");
                report.appendChild(contacts);
                ADNCollection adnc = new ADNCollection(apduSender);
                for(int i=0; i<adnc.getNumRegs(); i++)
                {
                    
                    Element contact = doc.createElement("contact");
                    contacts.appendChild(contact);
                    
                    Element id = doc.createElement("id");
                    contact.appendChild(id);
                    id.appendChild(doc.createTextNode(""+(i+1)));
                    
                    Element name = doc.createElement("name");
                    contact.appendChild(name);
                    name.appendChild(doc.createTextNode(adnc.getNome(i)));
                    
                    Element memory = doc.createElement("memory");
                    contact.appendChild(memory);
                    memory.appendChild(doc.createTextNode("SIM"));

                    
                    Element phoneNumber = doc.createElement("phone_number");
                    contact.appendChild(phoneNumber);
                    
                    
                    Element designation = doc.createElement("designation");
                    phoneNumber.appendChild(designation);
                    designation.appendChild(doc.createTextNode("Mobile"));

                    Element value = doc.createElement("value");
                    phoneNumber.appendChild(value);
                    value.appendChild(doc.createTextNode(adnc.getNumero(i)));
                    
                    
                }
                
                //SMS
                SMSCollection smsc = new SMSCollection(apduSender, adnc);
                Element smsMessages = doc.createElement("sms_messages");
                report.appendChild(smsMessages);
                int j=0;
                for(int i=0; i<smsc.getNumRegs(); i++)
                {
                    if(!smsc.getRegistroApagado(i))
                    {
                        Element smsMessage = doc.createElement("sms_message");
                        smsMessages.appendChild(smsMessage);

                        Element id = doc.createElement("id");
                        smsMessage.appendChild(id);
                        id.appendChild(doc.createTextNode(""+(++j)));

                        Element number = doc.createElement("number");
                        smsMessage.appendChild(number);
                        number.appendChild(doc.createTextNode(smsc.getNumero(i)));

                        Element name = doc.createElement("name");
                        smsMessage.appendChild(name);
                        name.appendChild(doc.createTextNode(smsc.getNome(i)));

                        Element status = doc.createElement("status");
                        smsMessage.appendChild(status);
                        status.appendChild(doc.createTextNode(smsc.getEstado(i)));

                        Element folder = doc.createElement("folder");
                        smsMessage.appendChild(folder);
                        folder.appendChild(doc.createTextNode(smsc.getPasta(i)));

                        Element storage = doc.createElement("storage");
                        smsMessage.appendChild(storage);
                        storage.appendChild(doc.createTextNode("SIM"));

                        Element type = doc.createElement("type");
                        smsMessage.appendChild(type);
                        type.appendChild(doc.createTextNode(smsc.getTipo(i)));

                        Element text = doc.createElement("text");
                        smsMessage.appendChild(text);
                        String texto = smsc.getTexto(i);
                        texto = textSafe(texto);
                        text.appendChild(doc.createTextNode(texto));
                        
                        Element timestamp = doc.createElement("timestamp");
                        smsMessage.appendChild(timestamp);
                        timestamp.appendChild(doc.createTextNode(smsc.getDataHora(i)));

                        Element smscnumber = doc.createElement("smsc");
                        smsMessage.appendChild(smscnumber);
                        smscnumber.appendChild(doc.createTextNode(smsc.getNumero(i)));
                    }
                    
                }

                //SMS
                smsc = new SMSCollection(apduSender, adnc);
                Element deletedSmsMessages = doc.createElement("deleted_sms_messages");
                report.appendChild(deletedSmsMessages);
                j=0;
                for(int i=0; i<smsc.getNumRegs(); i++)
                {
                    if(smsc.getRegistroApagado(i))
                    {
                        Element smsMessage = doc.createElement("sms_message");
                        deletedSmsMessages.appendChild(smsMessage);

                        Element id = doc.createElement("id");
                        smsMessage.appendChild(id);
                        id.appendChild(doc.createTextNode(""+(++j)));

                        Element number = doc.createElement("number");
                        smsMessage.appendChild(number);
                        number.appendChild(doc.createTextNode(smsc.getNumero(i)));

                        Element name = doc.createElement("name");
                        smsMessage.appendChild(name);
                        name.appendChild(doc.createTextNode(smsc.getNome(i)));

                        Element status = doc.createElement("status");
                        smsMessage.appendChild(status);
                        status.appendChild(doc.createTextNode(smsc.getEstado(i)));

                        Element folder = doc.createElement("folder");
                        smsMessage.appendChild(folder);
                        folder.appendChild(doc.createTextNode(smsc.getPasta(i)));

                        Element storage = doc.createElement("storage");
                        smsMessage.appendChild(storage);
                        storage.appendChild(doc.createTextNode("SIM"));

                        Element type = doc.createElement("type");
                        smsMessage.appendChild(type);
                        type.appendChild(doc.createTextNode(smsc.getTipo(i)));

                        Element text = doc.createElement("text");
                        smsMessage.appendChild(text);
                        String texto = smsc.getTexto(i);
                        text.appendChild(doc.createTextNode(texto));
                        
                        Element timestamp = doc.createElement("timestamp");
                        smsMessage.appendChild(timestamp);
                        timestamp.appendChild(doc.createTextNode(smsc.getDataHora(i)));
                        

                        Element smscnumber = doc.createElement("smsc");
                        smsMessage.appendChild(smscnumber);
                        smscnumber.appendChild(doc.createTextNode(smsc.getNumero(i)));
                    }
                    
                }
                
                
                //Registros de chamadas realizadas
                LNDCollection lndc = new LNDCollection(apduSender);
                Element outgoingCalls = doc.createElement("outgoing_calls");
                report.appendChild(outgoingCalls);
                for(int i=0; i<lndc.getNumRegs(); i++)
                {
                    Element outgoingCall = doc.createElement("outgoing_call");
                    outgoingCalls.appendChild(outgoingCall);
                    
                    Element id = doc.createElement("id");
                    outgoingCall.appendChild(id);
                    id.appendChild(doc.createTextNode(""+(i+1)));
                    
                    Element type = doc.createElement("type");
                    outgoingCall.appendChild(type);
                    type.appendChild(doc.createTextNode("Outgoing"));

                    Element name = doc.createElement("name");
                    outgoingCall.appendChild(name);
                    name.appendChild(doc.createTextNode(lndc.getNome(i)));
                    
                    
                    Element number = doc.createElement("number");
                    outgoingCall.appendChild(number);
                    number.appendChild(doc.createTextNode(lndc.getNumero(i)));
                    
                    Element timestamp = doc.createElement("timestamp");
                    outgoingCall.appendChild(timestamp);
                    timestamp.appendChild(doc.createTextNode("N/A"));
                    
                    Element duration = doc.createElement("duration");
                    outgoingCall.appendChild(duration);
                    duration.appendChild(doc.createTextNode("N/A"));
                    
                }


                //Informações gerais
                EF ef;
                Element generalInformation = doc.createElement("general_information");
                report.appendChild(generalInformation);
                
                Element iccid = doc.createElement("iccid");
                generalInformation.appendChild(iccid);
                ef = new EF(apduSender, "ICCID", new String[]{"MF"});
                iccid.appendChild(doc.createTextNode( (new DecodeICCID() ).decode(ef.getContentsTransparent()) ));
                
                Element imsi = doc.createElement("imsi");
                generalInformation.appendChild(imsi);
                ef = new EF(apduSender, "IMSI", new String[]{"MF","GSM"});
                imsi.appendChild(doc.createTextNode( (new DecodeIMSI() ).decode(ef.getContentsTransparent()) ));
                
                Element spn = doc.createElement("spn");
                generalInformation.appendChild(spn);
                ef = new EF(apduSender, "SPN", new String[]{"MF","GSM"});
                DecodeSPN decodeSPN = new DecodeSPN();
                decodeSPN.decode(ef.getContentsTransparent());
                spn.appendChild(doc.createTextNode(decodeSPN.getSPNName()));
               
                
                //msisdn
                MSISDNCollection msisdnc = new MSISDNCollection(apduSender);
                if(msisdnc.getNumRegs() > 0)
                {
                    Element msisdnInformation = doc.createElement("msisdn_information");
                    report.appendChild(msisdnInformation);

                    for(int i=0;i<msisdnc.getNumRegs();i++)
                    {
                        Element msisdn = doc.createElement("msisdn");
                        msisdnInformation.appendChild(msisdn);

                        Element id = doc.createElement("id");
                        msisdn.appendChild(id);
                        id.appendChild(doc.createTextNode(""+(i+1)));

                        Element name = doc.createElement("name");
                        msisdn.appendChild(name);
                        name.appendChild(doc.createTextNode(msisdnc.getNome(i)));

                        Element number = doc.createElement("number");
                        msisdn.appendChild(number);
                        number.appendChild(doc.createTextNode(msisdnc.getNumero(i)));

                    }
                }

                // write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(selectedFile);
 
		transformer.transform(source, result);
                
                
                
    }
    
    private static String textSafe(String input) 
    {
        if (input == null) return null;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length()-1; i++) {

            if (Character.isHighSurrogate(input.charAt(i))) 
                i += 1; //also skip the second character of the emoji
            else
                sb.append(input.charAt(i));
        }

        return sb.toString();
    }
}
