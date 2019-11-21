/*
 * The MIT License
 *
 * Copyright 2018 Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>.
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

package simexplorer.decoders;

import com.googlecode.attention.sms.pdu.PDUElement;
import com.googlecode.attention.sms.pdu.SMSDeliver;
import com.googlecode.attention.sms.pdu.SMSSubmit;
import com.googlecode.attention.sms.pdu.TPDU;
import com.googlecode.attention.sms.pdu.VP;
import com.googlecode.attention.sms.pdu.VPF;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>
 */
public class DecodeSMS implements DecodeEF{

    private static boolean showTimeZone = true;
    
    public static boolean getShowTimeZone()
    {
        return showTimeZone;
    }
    
    public static void setShowTimeZone(boolean showTimeZone)
    {
        DecodeSMS.showTimeZone = showTimeZone;
    }
    
    private boolean registroUsado = false;
    private Status status = Status.Invalido;
    private Report report = Report.Invalid;
    private TypeOfNumber smscton = TypeOfNumber.invalid;
    private NumberingPlanIdentification smscnpi = NumberingPlanIdentification.invalid;
    private String smscnumber = "";
    private MessageTypeIndicator messageTypeIndicator = MessageTypeIndicator.Invalid;
    private TPDU tpdu = null;
    
    //para relatorio
    private String numero="";
    private String estado="";
    private String pasta="";
    private String tipo="";
    private String texto="";
    private String smsc="";
    private boolean registroValido = false;
    private boolean registroApagado = false;
    private String datahora = "";
    
    public String getDataHora()
    {
        return datahora;
    }
    
    public boolean getRegistroApagado()
    {
        return registroApagado;
    }
    
    public boolean getRegistroValido()
    {
        return registroValido;
    }

    public String getEstado() {
        return estado;
    }

    public String getNumero() {
        return numero;
    }



    public String getPasta() {
        return pasta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTexto() {
        return texto;
    }

    public String getSmsc() {
        return smsc;
    }
    
    private void init()
    {
        registroUsado = false;
        status = Status.Invalido;
        report = Report.Invalid;
        smscton = TypeOfNumber.invalid;
        smscnpi = NumberingPlanIdentification.invalid;
        smscnumber = "";   
        messageTypeIndicator = MessageTypeIndicator.Invalid;
        tpdu = null;
    }
    
    @Override
    public String decode(byte[] bytes) throws IllegalArgumentException {
        
        init();
        
        byte s = bytes[0];
        registroUsado = (1&s)==1;
        registroValido = registroUsado;
        switch((s & 0x7)|1) //adicionar 1 para contemplar os registros apagados
        {
            case 1: status = Status.RecebidaLida; estado = "Read"; pasta = "Inbox"; tipo="Incoming"; break;
            case 3: status = Status.RecebidaNaoLida; estado =  "Unread"; pasta = "Inbox"; tipo = "Incoming"; break;
            case 7: status = Status.OriginadaNaoEnviada; estado = "Unsent"; pasta = "Outbox"; tipo = "Outgoing"; break;
            case 5: status = Status.OriginadaEnviada; estado = "Sent"; pasta = "Sent"; tipo = "Outgoing"; break;
            default: status = Status.Invalido; 
        }
        
        switch((s & 31) | 1)//adicionar 1 para contemplar os registros apagados
        {
            case 5:  report = Report.NotRequested; break;
            case 13: report = Report.RequestedNotReceived; break;
            case 21: report = Report.RequestedReceivedNotStored; break;
            case 29: report = Report.RequestedReceivedStored; break;
            default: report = Report.Invalid;
        }
        
        int smsc_len = 0xFF & bytes[1];
        if(smsc_len != 0xFF) 
        {

            registroApagado = !registroUsado;
                    
            if(smsc_len != 0)
            {
                byte tonnpi = bytes[2];

                smscton = extractTypeOfNumber(tonnpi);
                smscnpi = extractNumberingPlanIdentification(tonnpi);

                smscnumber = "";
                for(int i=1; i<smsc_len;i++)
                {
                    if((0x0F & (bytes[2+i])) != 0xF) smscnumber += (0x0F & (bytes[2+i]));
                    if((0x0F & (bytes[2+i] >> 4)) != 0xF) smscnumber += (0x0F & (bytes[2+i] >> 4));
                }
            }
            smsc = smscnumber;
            
            byte mti = (byte)(bytes[smsc_len+2] & 0x3);
            if(this.status == Status.RecebidaLida || this.status == Status.RecebidaNaoLida)
            {
                switch(mti)
                {
                    case 0: messageTypeIndicator = MessageTypeIndicator.SMS_DELIVER; break;
                    case 2: messageTypeIndicator = MessageTypeIndicator.SMS_STATUS_REPORT; break;
                    case 1: messageTypeIndicator = MessageTypeIndicator.SMS_SUBMIT_REPORT; break;
                    default: messageTypeIndicator = MessageTypeIndicator.Invalid;
                }
            }
            else if(this.status == Status.OriginadaEnviada || this.status == Status.OriginadaNaoEnviada)
            {
                switch(mti)
                {
                    case 0: messageTypeIndicator = MessageTypeIndicator.SMS_DELIVER_REPORT; break;
                    case 2: messageTypeIndicator = MessageTypeIndicator.SMS_COMMAND; break;
                    case 1: messageTypeIndicator = MessageTypeIndicator.SMS_SUBMIT; break;
                    default: messageTypeIndicator = MessageTypeIndicator.Invalid;
                }
            }
            else
                messageTypeIndicator = MessageTypeIndicator.Invalid;
            
            
            //montar tpdu a partir do byte len+2
            int[] tpduraw = new int[bytes.length-smsc_len+2];
            for(int i=smsc_len+2;i<bytes.length; i++)
            {
                tpduraw[i-(smsc_len+2)] = bytes[i];
            }
        
            switch(messageTypeIndicator)
            {
                case SMS_SUBMIT:
                {
                    SMSSubmit smsSubmit = new SMSSubmit(tpduraw);
                    smsSubmit.decode();
                    tpdu = smsSubmit;
                    break;
                }
                case SMS_DELIVER:
                {
                    SMSDeliver smsDeliver = new SMSDeliver(tpduraw);
                    smsDeliver.decode();
                    tpdu = smsDeliver;
                    break;
                }
                    
            }
            
            
            
            
        }
        
        
        
        StringBuilder sb = new StringBuilder();
        sb.append(registroUsado?"Register used":"Register not used or deleted").append("\n");
        sb.append(status).append("\n");
        sb.append(report);
        sb.append("\nSMSC Type of number: ").append(smscton);
        sb.append("\nSMSC Numbering Plan Identification: ").append(smscnpi);
        sb.append("\nSMSC number: ").append(smscnumber);
        sb.append(("\nMessage Type Indicator: " )).append(messageTypeIndicator);
        switch(messageTypeIndicator)
        {
            case SMS_SUBMIT:
            {
                SMSSubmit smsSubmit = (SMSSubmit) tpdu;
                sb.append("\nDestination Type of number: ").append(extractTypeOfNumber((byte)(smsSubmit.getDestinationAddress().getTOA().getValue())));
                sb.append("\nDestination Numbering Plan Identification: ").append(extractNumberingPlanIdentification((byte)(smsSubmit.getDestinationAddress().getTOA().getValue())));
                sb.append("\nDestination: " ).append(smsSubmit.getDestinationAddress().getNumber());
                numero = smsSubmit.getDestinationAddress().getNumber();
                sb.append("\nValidity Period: ").append(validityPeriodToString(smsSubmit.getValidityPeriod()));
                datahora = "N/A";
                sb.append("\nMessage reference:").append(smsSubmit.get(PDUElement.TP_MR));
                sb.append("\nMessage: ").append(smsSubmit.getMessage());
                texto = smsSubmit.getMessage();
                break;
            }
            case SMS_DELIVER:
            {
                SMSDeliver smsDeliver = (SMSDeliver) tpdu;
                sb.append("\nOrigin: " ).append(smsDeliver.getOriginatingAddress().getNumber());
                numero = smsDeliver.getOriginatingAddress().getNumber();
                SimpleDateFormat format;
                if(showTimeZone)
                    format = new SimpleDateFormat("M/dd/yyyy k:mm:ss 'GMT' XXX");
                else
                    format = new SimpleDateFormat("M/dd/yyyy k:mm:ss");
                Calendar c = smsDeliver.getServiceCentreTimeStamp().getDate();
                format.setTimeZone(c.getTimeZone());
                sb.append("\nDate: " ).append(format.format(c.getTime()));
                datahora = format.format(smsDeliver.getServiceCentreTimeStamp().getDate().getTime());
                sb.append("\nMore messages to send: ").append(!(smsDeliver.get(PDUElement.TP_MMS)==0X4));
                sb.append("\nTP-UD contains a Header: ").append(smsDeliver.get(PDUElement.TP_UDHI)==0x40);
                sb.append("\nMessage: ").append(smsDeliver.getMessage());
                texto = smsDeliver.getMessage();
                smsDeliver.getMessage();
                break;
            }
            default:
            {
                sb.append(messageTypeIndicator);
            }
        }
        
        return sb.toString();
        
    }
    
    private String validityPeriodToString(VP validityPeriod)
    {
        //TODO: ver 3GPP TS 23.040 para melhor decodificação
        final StringBuilder sb = new StringBuilder();
        final VPF vpf= validityPeriod.getVPF();
        sb.append("[VPF:");
        
        sb.append(validityPeriod.getVPF().toString());

        switch (vpf) {
        case RELATIVE_FORMAT:
          sb.append("][");
          sb.append(validityPeriod.getRelativeTime().toString());
          break;

        case ABSOLUTE_FORMAT:
          sb.append("][");
          sb.append(validityPeriod.getAbsoluteTime().toString());
          break;

        case ENHANCED_FORMAT:
          sb.append("][");
          sb.append(validityPeriod.getEnhancedTime().toString());
          break;

        case FIELD_NOT_PRESENT: /* falls through */
        default:
          break;
        }
        sb.append(']'); // closes either vpf or vp
        return sb.toString();
    }
    
    
    private TypeOfNumber extractTypeOfNumber(byte tonnpi)
    {
        byte ton = (byte) (0x07 & (tonnpi>>4));
        
        
        switch(ton)
        {
            case 0: return TypeOfNumber.unknown; 
            case 1: return TypeOfNumber.international;
            case 2: return TypeOfNumber.national; 
            case 3: return TypeOfNumber.networkspecific;
            case 4: return TypeOfNumber.dedicatedaccess_shortcode; 
            default: return TypeOfNumber.invalid;
        }


    }
    
    private NumberingPlanIdentification extractNumberingPlanIdentification (byte tonnpi)
    {
                byte npi = (byte) (0x0F & tonnpi);

                switch (npi)
                {
                    case 0: return NumberingPlanIdentification.unknown; 
                    case 1: return NumberingPlanIdentification.ISDN; 
                    case 3: return NumberingPlanIdentification.data; 
                    case 4: return NumberingPlanIdentification.telex; 
                    case 8: return NumberingPlanIdentification.national; 
                    case 9: return NumberingPlanIdentification.privatenumbering; 
                    default : return NumberingPlanIdentification.invalid;
                }
        
    }
    
    public enum Status{
        RecebidaLida, RecebidaNaoLida, OriginadaEnviada, OriginadaNaoEnviada, Invalido;
        
        @Override
        public String toString()
        {
            switch(this)
            {
                case RecebidaLida: return "Received/Read";
                case RecebidaNaoLida: return "Received/Not read";
                case OriginadaEnviada: return "Originada pelo celular / Enviada";
                case OriginadaNaoEnviada: return "Originada pelo celular / Não Enviada";
                default: return "Status inválido";
            }
        }
        
        
    }
    
    public enum Report{
        NotRequested, RequestedNotReceived, RequestedReceivedNotStored, RequestedReceivedStored, Invalid;
        
        @Override
        public String toString()
        {
            switch(this)
            {
                case NotRequested: return "Status report not requested";
                case RequestedNotReceived: return "Status report requested but not (yet) received";
                case RequestedReceivedNotStored: return "Status report requested, received but not stored in EF=SMSR";
                case RequestedReceivedStored: return "Status report requested, received and stored in EF=SMSR";
                default: return "Invalid status report";
                        
            }
        }
    }
    
    public enum TypeOfNumber{
        unknown, international, national, networkspecific, dedicatedaccess_shortcode, invalid;
        
        @Override
        public String toString()
        {
            
            switch(this)
            {
                
                case unknown: return "unkown";
                case international: return "international number"; 
                case national: return "national number"; 
                case networkspecific: return "network specif number"; 
                case dedicatedaccess_shortcode: return "dedicated access, short code"; 
                default: return "invalid";
            }
        }
    }
    
    public enum NumberingPlanIdentification{
        unknown, ISDN, data, telex, national, privatenumbering, invalid;
        
        @Override
        public String toString()
        {
            
            switch(this)
            {
                
                case unknown: return "unkown";
                case ISDN: return "ISDN/telephony numbering plan"; 
                case data: return "data numbering plan"; 
                case telex: return "telex numbering plan"; 
                case national: return "national numbering plan"; 
                case privatenumbering: return "private numbering plan"; 
                default: return "invalid";
            }
        }
    }
    
    
    public enum MessageTypeIndicator{
        SMS_DELIVER, 
        SMS_SUBMIT, 
        SMS_DELIVER_REPORT, 
        SMS_SUBMIT_REPORT, 
        SMS_STATUS_REPORT, 
        SMS_COMMAND, 
        Invalid;

        @Override
        public String toString()
        {
            switch(this)
            {
                case SMS_DELIVER: return "SMS-DELIVER";
                case SMS_SUBMIT: return "SMS-SUBMIT";
                case SMS_DELIVER_REPORT:return "SMS-DELIVER-REPORT";
                case SMS_SUBMIT_REPORT:return "SMS-SUBMIT-REPORT";
                case SMS_STATUS_REPORT: return "SMS-STATUS-REPORT";
                case SMS_COMMAND:return "SMS-COMMAND";
                default: return "invalid";       
            }
        }
      
    }

    
    
}
