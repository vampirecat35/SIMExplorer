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

package simexplorer.files;

import java.util.ArrayList;
import simexplorer.apdusender.APDUSender;
import simexplorer.decoders.DecodeEF;
import simexplorer.decoders.DecodeEFFactory;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>
 */
public class EF extends File {
    
    final private Boolean invalidated;
    final private Boolean readableAndUpdatableWhenInvalidated;
    final private StructureOfFile structureOfFile;
    final private int fileSize;
    final protected int lengthOfARecord;
    final protected int numRegs;
    final private IncreaseCommandAllowed increaseCommandAllowed;
    
    final private AccessConditions updateAccessConditions;
    final private AccessConditions readSeekAccessConditions;
    final private AccessConditions increaseAccessConditions;
    final private AccessConditions invalidateAccessConditions;
    final private AccessConditions rehabilitateAccessConditions;

    protected byte[] contentsTranparent;
    protected byte[][] contentsLinearCyclic;
    
    private boolean erroLeitura;
    
    final private APDUSender apduSender;
    
    public EF(APDUSender apduSender, String nome, String[] pais) throws SIMFileNotFoundException {
        super(apduSender, nome, pais);
        this.apduSender = apduSender;
        
        fileSize = (char) (((resposta[2] << 8) | (0x00FF & resposta[3])));

        switch(resposta[13])
        {
            case 0: structureOfFile = StructureOfFile.Transparent; break;
            case 1: structureOfFile = StructureOfFile.LinearFixed; break;
            case 3: structureOfFile = StructureOfFile.Cyclic; break;
            default: structureOfFile = StructureOfFile.Invalid;
        }

        if(structureOfFile == StructureOfFile.Cyclic)
        {
            if((resposta[7] & 0x40) == 0x40)
                increaseCommandAllowed = IncreaseCommandAllowed.TRUE;
            else
                increaseCommandAllowed = IncreaseCommandAllowed.FALSE;

        }
        else
            increaseCommandAllowed = IncreaseCommandAllowed.NOT_APPLY;

        byte update = (byte) (resposta[8] & 0xF);
        byte readSeek = (byte) ((resposta[8] >> 4) &0xF);
        byte increase = (byte) ((resposta[9] >> 4) &0xF);
        byte invalidate = (byte) (resposta[10] & 0xF);
        byte rehabilitate = (byte) ((resposta[10] >> 4) &0xF);

        updateAccessConditions = AccessConditionsMap.getAcessConditions(update);
        readSeekAccessConditions = AccessConditionsMap.getAcessConditions(readSeek);
        increaseAccessConditions = AccessConditionsMap.getAcessConditions(increase);
        invalidateAccessConditions = AccessConditionsMap.getAcessConditions(invalidate);
        rehabilitateAccessConditions = AccessConditionsMap.getAcessConditions(rehabilitate);

        byte fileStatus = resposta[11];

        invalidated = ((fileStatus & 0x1) == 0x0);
        readableAndUpdatableWhenInvalidated = ((fileStatus & 0x4) == 0x4);

        if(structureOfFile == StructureOfFile.Transparent)
        {
            resposta = apduSender.enviarAPDU(new byte[]{(byte)0xA0, (byte)0xB0, 0x0, 0x0, (byte)fileSize});
            if(resposta[resposta.length-2] == (byte)0x90 && resposta[resposta.length-1] == 0x00)
            {
                erroLeitura = false;
                contentsTranparent = new byte[fileSize];
                for(int i=0; i<contentsTranparent.length;i++)
                    contentsTranparent[i] = resposta[i];
            }
            else
            {
                erroLeitura = true;
                contentsTranparent = new byte[0];
            }
            lengthOfARecord = 0;
            numRegs = 0;
        }

        else if(structureOfFile == StructureOfFile.LinearFixed || structureOfFile == StructureOfFile.Cyclic)
        {
            lengthOfARecord = 0xFF & resposta[14];
            numRegs = fileSize/lengthOfARecord;
            contentsLinearCyclic = new byte[numRegs][lengthOfARecord];
            for(int i=0; i<numRegs; i++)
            {
                resposta = apduSender.enviarAPDU(new byte[]{(byte)0xA0, (byte)0xB2, (byte) (i+1), 0x04, (byte)lengthOfARecord});
                if(resposta.length != 2)
                {
                    erroLeitura = false;
                    contentsLinearCyclic[i] = new byte[lengthOfARecord];
                    for(int j=0; j<lengthOfARecord; j++)
                        contentsLinearCyclic[i][j] = resposta[j];
                }
                else
                {
                    erroLeitura = true;
                }
            }
        }
        else
        {
            lengthOfARecord = 0;
            numRegs = 0;
            erroLeitura = false;
        }
        
    }
    
    public int getFileSize()
    {
        return fileSize;
    }

    public Boolean getInvalidated()
    {
        return invalidated;
    }
    public Boolean getReadableAndUpdatableWhenInvalidated()
    {
        return readableAndUpdatableWhenInvalidated;
    }
    public StructureOfFile getStructureOfFile()
    {
        return structureOfFile;
    }

    public IncreaseCommandAllowed getIncreaseCommandAllowed()
    {
        return increaseCommandAllowed;
    }
    
    public AccessConditions getUpdateAccessConditions()
    {
        return updateAccessConditions;
    }
    public AccessConditions getReadSeekAccessConditions()
    {
        return readSeekAccessConditions;
    }
    public AccessConditions getIncreaseAccessConditions()
    {
        return increaseAccessConditions;
    }
    public AccessConditions getInvalidateAccessConditions()
    {
        return invalidateAccessConditions;
    }
    public AccessConditions getRehabilitateAccessConditions()  
    {
        return rehabilitateAccessConditions;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb ;
        sb = new StringBuilder();
        
        sb.append("File ID: ");
        sb.append(String.format("%02X", fileID[0]));
        sb.append(" ");
        sb.append(String.format("%02X", fileID[1])) ;

        sb.append("\nFile size: " + fileSize);
        if(structureOfFile == StructureOfFile.LinearFixed || structureOfFile == StructureOfFile.Cyclic)
            sb.append("\nLength of a record: " + lengthOfARecord);
        else
            sb.append("\nLength of a record: not apply");
        sb.append("\nType of file: " + typeOfFile);
        sb.append("\nInvalidated: " + invalidated);
        sb.append("\nReadable and updatable when invalidated: " + readableAndUpdatableWhenInvalidated);
        sb.append("\nStructured of file: " + structureOfFile);
        sb.append("\nIncrease command allowed " + increaseCommandAllowed);
        sb.append("\nIncrease access conditions:" + increaseAccessConditions);
        sb.append("\nRead/Seek access conditions:" + readSeekAccessConditions);
        sb.append("\nUpdate access conditions:" + updateAccessConditions);
        sb.append("\nInvalidate access conditions:" + invalidateAccessConditions);
        sb.append("\nRehabilitate access conditions:" + rehabilitateAccessConditions);

        if(!erroLeitura)
        {
            
            
            if(this.structureOfFile == StructureOfFile.Transparent)
            {
                sb.append("\n\n\nContent:\n\n");
                sb.append(formatBytes(this.contentsTranparent));
            }
            else if(this.structureOfFile == StructureOfFile.LinearFixed || this.structureOfFile == StructureOfFile.Cyclic)
            {
                sb.append("\n\n\nContent:\n\n");
                for(int i=0; i<numRegs; i++)
                {
                    sb.append("Register " + (i+1) + "\n\n");
                    sb.append(formatBytes(this.contentsLinearCyclic[i]));
                    sb.append("\n\n\n");
                }
            }

            return sb.toString();
        }
        else
        {
            sb.append("\n\n\nReading Error");
            if(resposta[0] == (byte)0x98)
            {
                switch(resposta[1])
                {
                    case (byte)0x02:
                        sb.append("\n-no CHV initialised");
                        break;
                    case (byte)0x04:
                        sb.append("\n-access condition not fulfilled");
                        sb.append("\n-unsuccessful CHV verification, at least one attempt left");
                        sb.append("\n-unsuccessful UNBLOCK CHV verification, at least one attempt left");
                        sb.append("\n-authentication failed (A Phase 1 SIM may send this error code after the third consecutive unsuccessful CHV verification attempt or the tenth consecutive unsuccessful unblocking attempt.)");
                        break;
                    case (byte)0x08:
                        sb.append("\n-in contradiction with CHV status");
                        break;
                    case (byte)0x10:
                        sb.append("\n-in contradiction with invalidation status");
                        break;
                    case (byte)0x40:
                        sb.append("\n-unsuccessful CHV verification, no attempt left");
                        sb.append("\n-unsuccessful UNBLOCK CHV verification, no attempt left");
                        sb.append("\n-CHV blocked");
                        sb.append("\n-UNBLOCK CHV blocked");
                        break;
                    case (byte)0x50:
                        sb.append("increase cannot be performed, Max value reached");
                        break;
                    
                }
            }
            return sb.toString();
        }
        
    }
    
    public byte[] getContentsLinearCyclic(int i)
    {
        if(structureOfFile == StructureOfFile.LinearFixed || structureOfFile == StructureOfFile.Cyclic)
        {
            if(i<0 || i>= this.getNumRegs())
            {
                throw new IndexOutOfBoundsException("i = " + i + " ; numRegs = " + numRegs);
            }
            return contentsLinearCyclic[i];
        }
        else
            return null;
    }
    
    public String getContentsLinearCyclicAsString(int i)
    {
        byte[] bytes = getContentsLinearCyclic(i);
        StringBuilder strBuff = new StringBuilder("");  
        for (int j = 0; j < bytes.length; j++) {  
            if(j%16 == 0) strBuff.append("\n");
            strBuff.append(String.format("%02X ", bytes[j])); 
        }  
        return strBuff.toString();  
        
    }
    
    
    public byte[] getContentsTransparent()
    {
        if(this.structureOfFile == StructureOfFile.Transparent)
            return contentsTranparent;
        else
            return null;
    }

    public String getContentsTransparentAsString()
    {
        byte[] bytes = getContentsTransparent();
        StringBuilder strBuff = new StringBuilder("");  
        for (int i = 0; i < bytes.length; i++) {  
            if(i%16 == 0) strBuff.append("\n");
            strBuff.append(String.format("%02X ", bytes[i])); 
        }  
        return strBuff.toString();  
        
    }
    
    private static String formatBytes(byte[] bytes)  
    {  
        StringBuilder strBuff = new StringBuilder("");  
        for (int i = 0; i < bytes.length; i++) {  
            if(i%16 == 0) strBuff.append("\n");
            strBuff.append(String.format("%02X ", bytes[i])); 
        }  
        return strBuff.toString();  
    }  

    public String dadosDecodificados()
    {
        if(this.structureOfFile == StructureOfFile.Transparent)
        {
            DecodeEF decodeEF = DecodeEFFactory.getDecodeEF(nome);
            if(decodeEF != null && !this.erroLeitura) 
                return decodeEF.decode(contentsTranparent);
            else
                return "Decoding not available";
        }
        else if(this.structureOfFile == StructureOfFile.Cyclic || this.structureOfFile == StructureOfFile.LinearFixed)
        {
            DecodeEF decodeEF = DecodeEFFactory.getDecodeEF(nome);
            if(decodeEF != null && !this.erroLeitura)
            {
                StringBuilder sb = new StringBuilder();
                int numRegs = fileSize/lengthOfARecord;
                for(int i=0; i<numRegs; i++)
                {
                    sb.append("Register " + (i+1) + "\n\n");
                    try
                    {
                        sb.append(decodeEF.decode(contentsLinearCyclic[i]));
                    }
                    catch(Exception ex)
                    {
                        sb.append("Decoding error: " + ex.toString() );
                    }
                    sb.append("\n\n\n");
                }
            
                return sb.toString();
            }
            else return "Decoding not available";
        }
        else
            return "";
        
    }

    public void updateBinary(String texto) throws UpdateBinaryException {
        texto = texto.trim();
        
        String[] textos_tmp =  texto.split("\\s+");
        ArrayList textos = new ArrayList();
        
        for(int i=0;i<textos_tmp.length;i++)
            if (!"".equals(textos_tmp[i].trim()))  textos.add(textos_tmp[i].trim());
        
        byte buffer[] = new byte[textos.size()];
        
        try
        {
            for(int i=0; i<buffer.length;i++)
                buffer[i] = (byte)(int)Integer.valueOf(String.valueOf(textos.get(i)), 16);
        }
        catch(Exception ex)
        {
            throw new UpdateBinaryException("Invalid text.\n" +  ex.getMessage());
        }
        
        updateBinary(buffer);
        
        
    }
    
    public void updateBinary(byte[] bytes) throws UpdateBinaryException 
    {
        if(this.structureOfFile == StructureOfFile.Transparent)
        {
            //supor já selecionado
            if(this.fileSize != bytes.length)
                throw new UpdateBinaryException("Data must have "  +  this.fileSize + " octets.");
            
            byte[] apdu = new byte[5 + this.fileSize];
            System.arraycopy(new byte[]{(byte)0xA0, (byte)0xD6, 0, 0, (byte) fileSize}, 0, apdu, 0, 5);
            System.arraycopy(bytes, 0, apdu, 5, fileSize);
            byte[] resposta = apduSender.enviarAPDU(apdu);
            
            if(resposta.length >= 2)
            {
                if(resposta[resposta.length-2] != (byte)0x90 || resposta[resposta.length-1] != (byte)0x00)
                    throw new UpdateBinaryException("updateBinary error: can not update value." );
            }
            else
                throw new UpdateBinaryException("updateBinary error: can not update value." );

        }
        else throw new UpdateBinaryException("This action only can be performed over a transparent EF.");
        
        
    }

    
    public void updateRecord(String texto, int numRec) throws UpdateRecordException {
        texto = texto.trim();
        
        String[] textos_tmp =  texto.split("\\s+");
        ArrayList textos = new ArrayList();
        
        for(int i=0;i<textos_tmp.length;i++)
            if (!"".equals(textos_tmp[i].trim()))  textos.add(textos_tmp[i].trim());
        
        byte buffer[] = new byte[textos.size()];
        
        try
        {
            for(int i=0; i<buffer.length;i++)
                buffer[i] = (byte)(int)Integer.valueOf(String.valueOf(textos.get(i)), 16);
        }
        catch(Exception ex)
        {
            throw new UpdateRecordException("Invalid text.\n" +  ex.getMessage());
        }
        
        updateRecord(buffer, numRec);
        
        
    }
    
    
    public void updateRecord(byte[] bytes, int numRec) throws UpdateRecordException 
    {
        if(this.structureOfFile == StructureOfFile.Cyclic && numRec != 0)
            throw new UpdateRecordException("NumRec must be 0 if structureOfFile == Cyclic.");
         
        if((numRec < 1 || numRec>numRegs) && this.structureOfFile == StructureOfFile.LinearFixed)
            throw new UpdateRecordException("NumRec must be between 1 and " + numRegs + ". Current value: " + numRec + ".");
            
        if(this.structureOfFile == StructureOfFile.Cyclic || this.structureOfFile == StructureOfFile.LinearFixed)
        {
            //supor já selecionado
            if(this.lengthOfARecord != bytes.length)
                throw new UpdateRecordException("Data must have "  +  this.lengthOfARecord + " octets.");
            
            byte[] apdu = new byte[5 + this.lengthOfARecord];
            if(this.structureOfFile == StructureOfFile.Cyclic) //ignorar numRec
                System.arraycopy(new byte[]{(byte)0xA0, (byte)0xDC, (byte)0, (byte)3, (byte) lengthOfARecord}, 0, apdu, 0, 5);
            else
                System.arraycopy(new byte[]{(byte)0xA0, (byte)0xDC, (byte)numRec, (byte)4, (byte) lengthOfARecord}, 0, apdu, 0, 5);
            System.arraycopy(bytes, 0, apdu, 5, lengthOfARecord);
            byte[] resposta = apduSender.enviarAPDU(apdu);
            if(resposta.length >= 2)
            {
                if(resposta[resposta.length-2] != (byte)0x90 || resposta[resposta.length-1] != (byte)0x00)
                    throw new UpdateRecordException("updateBinary error: can not update value." );
            }
            else
                throw new UpdateRecordException("updateBinary error: can not update value." );
            
            

        }
        else throw new UpdateRecordException("This action must be performed only over an EF Cyclic or LinearFixed.");
        
        
    }    
    
    public int getNumRegs() {
        if(structureOfFile == StructureOfFile.LinearFixed || structureOfFile == StructureOfFile.Cyclic)
            return numRegs;
        else
            return 0;
    }

    public static class UpdateBinaryException extends Exception {

        public UpdateBinaryException(String message) {
            super(message);
        }
    }

    public static class UpdateRecordException extends Exception {

        public UpdateRecordException(String message) {
            super(message);
        }
    }
    
    
    public enum StructureOfFile {
        Transparent, LinearFixed, Cyclic, Invalid;

        @Override
        public String toString()
        {
            if(this == Transparent)
                return "Transparent";
            else if(this == LinearFixed)
                return "Linear Fixed";
            else if(this == Cyclic)
                return "Cyclic";
            else
                return 
                        "Invalid";
        }
    }
    
}
