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


package simexplorer.decoders;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class DecodeADN implements DecodeEF{

    private String nome = "";
    private TypeOfNumber typeOfNumber;
    private NumberingPlanIdentification numberingPlanIdentification;
    private String numero = "";
    private short capabilityConfiguration = -1;
    private short extension1 = -1;
    
    
    
    private void init()
    {
        nome = "";
        typeOfNumber = TypeOfNumber.invalid;
        numberingPlanIdentification = NumberingPlanIdentification.invalid;
        numero = "";
        capabilityConfiguration = -1;
        extension1 = -1;        
    }
    
    @Override
    public String decode(byte[] bytes) {
        
        byte tonnpi;
        byte ton = 0;
        byte npi = 0;
        
        init();
        
        
        if(bytes[0] == (byte)0x80)
        {
            for(int i=1; i<bytes.length-14; i=i+2 )
            {
                if(bytes[i]!=(byte)0xFF)
                {
                    char c = (char)((bytes[i]<<8)&0xFF00);
                    c += (char)(bytes[i+1]&0x00FF);
                    if(c!=0) nome += c;
                }
            }
            
        }
        else if((byte)(0x80&bytes[0])==(byte)0x80)
        {
            nome = "";
        }
        else
        {
            for(int i=0; i<bytes.length-14; i++)
            {
                //if(bytes[i] != (byte)0xFF)
                    //if(bytes[i] != 0) nome += (char)bytes[i];
                if(GSM7CHARS[0xFF&bytes[i]] != -1)
                    if(bytes[i] != 0) nome += (char)GSM7CHARS[0xFF&bytes[i]];

            }
        }
        
        tonnpi = bytes [bytes.length-14 + 1];
        npi = (byte) (0x0F & tonnpi);
        ton = (byte) (0x07 & (tonnpi>>4));
        
        switch(ton)
        {
            case 0: typeOfNumber = TypeOfNumber.unknown; break;
            case 1: typeOfNumber = TypeOfNumber.international; break;
            case 2: typeOfNumber = TypeOfNumber.national; break;
            case 3: typeOfNumber = TypeOfNumber.networkspecific; break;
            case 4: typeOfNumber = TypeOfNumber.dedicatedaccess_shortcode; break;
            default: typeOfNumber = TypeOfNumber.invalid;
        }
        
        switch (npi)
        {
            case 0: numberingPlanIdentification = NumberingPlanIdentification.unknown; break;
            case 1: numberingPlanIdentification = NumberingPlanIdentification.ISDN; break;
            case 3: numberingPlanIdentification = NumberingPlanIdentification.data; break;
            case 4:numberingPlanIdentification = NumberingPlanIdentification.telex; break;
            case 8:numberingPlanIdentification = NumberingPlanIdentification.national; break;
            case 9: numberingPlanIdentification = NumberingPlanIdentification.privatenumbering; break;
            default : numberingPlanIdentification = NumberingPlanIdentification.invalid;
        }
        
        String digito;
        for(int i = 3; i<= 12; i++ )
        {
            if((0xF&(bytes[bytes.length-14 + i-1])) == 0xF) break;
            digito = String.format("%01X", 0xF&(bytes[bytes.length-14 + i-1]));
            
            if(digito.toUpperCase().equals("A"))
                digito = "*";
            if(digito.toUpperCase().equals("B"))
                digito = "#";
            numero += digito;
            if((0xF&(bytes[bytes.length-14 + i-1]>>4)) == 0xF) break;
            digito = String.format("%01X", 0xF&(bytes[bytes.length-14 + i-1]>>4));
            if(digito.toUpperCase().equals("A"))
                digito = "*";
            if(digito.toUpperCase().equals("B"))
                digito = "#";
            numero += digito;
        }
        if(typeOfNumber == TypeOfNumber.international)
            numero = "+" + numero;
        
        capabilityConfiguration = bytes [bytes.length-14 + 12];
        extension1 = bytes [bytes.length-14 + 13];
        
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + nome);
        sb.append("\nType of number: " + typeOfNumber);
        sb.append("\nNumbering plan identification: " + numberingPlanIdentification);
        sb.append("\nCapability configuration: " + ((capabilityConfiguration==-1)?"not used":capabilityConfiguration));
        sb.append("\nExtension1: " + ((extension1==-1)?"not used":extension1));
        sb.append("\nNumber: " + numero);
    
        return sb.toString();
        
    }
    
    public boolean isRegistroValido()
    {
        return !(nome.equals("") && numero.equals(""));
                
    }

    public String getNome()
    {
        return nome;
    }
    
    public String getNumero()
    {
        return numero;
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
    
    static final int[] GSM7CHARS = {
        0x0040, 0x00A3, 0x0024, 0x00A5, 0x00E8, 0x00E9, 0x00F9, 0x00EC,
        0x00F2, 0x00E7, 0x000A, 0x00D8, 0x00F8, 0x000D, 0x00C5, 0x00E5,
        0x0394, 0x005F, 0x03A6, 0x0393, 0x039B, 0x03A9, 0x03A0, 0x03A8,
        0x03A3, 0x0398, 0x039E, 0x00A0, 0x00C6, 0x00E6, 0x00DF, 0x00C9,
        0x0020, 0x0021, 0x0022, 0x0023, 0x00A4, 0x0025, 0x0026, 0x0027,
        0x0028, 0x0029, 0x002A, 0x002B, 0x002C, 0x002D, 0x002E, 0x002F,
        0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037,
        0x0038, 0x0039, 0x003A, 0x003B, 0x003C, 0x003D, 0x003E, 0x003F,
        0x00A1, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047,
        0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D, 0x004E, 0x004F,
        0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057,
        0x0058, 0x0059, 0x005A, 0x00C4, 0x00D6, 0x00D1, 0x00DC, 0x00A7,
        0x00BF, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067,
        0x0068, 0x0069, 0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F,
        0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077,
        0x0078, 0x0079, 0x007A, 0x00E4, 0x00F6, 0x00F1, 0x00FC, 0x00E0,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,};    
    
}
