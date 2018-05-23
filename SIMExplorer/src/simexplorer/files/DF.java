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

package simexplorer.files;

import simexplorer.apdusender.APDUSender;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class DF extends File{

    private final boolean isCHV1Enabled;

    public boolean isCHV1Enabled() {
        return isCHV1Enabled;
    }

    public boolean isCHV1Blocked() {
        return isCHV1Blocked;
    }

    public boolean isUnblockCHVIBlocked() {
        return isUnblockCHVIBlocked;
    }

    public boolean isCHV2Blocked() {
        return isCHV2Blocked;
    }

    public boolean isUnblockCHV2Blocked() {
        return isUnblockCHV2Blocked;
    }
    private final boolean isCHV1Blocked;
    private final boolean isUnblockCHVIBlocked;
    private final boolean isCHV2Blocked;
    private final boolean isUnblockCHV2Blocked;
    
    
    
    public DF(APDUSender apduSender, String nome, String[] pais) throws SIMFileNotFoundException {
        super(apduSender, nome, pais);
        
        byte chv1Status = this.resposta[18];
        isCHV1Blocked = (chv1Status & 0xF)==0;
        byte fileCharac = this.resposta[13];
        isCHV1Enabled = !((byte)(fileCharac & 0x80) == (byte)0x80);
        byte puk1Status = this.resposta[19];
        isUnblockCHVIBlocked = ((puk1Status & 0xF)==0);
        byte chv2Status = this.resposta[20];
        isCHV2Blocked = (chv2Status & 0xF)==0;
        byte puk2Status = this.resposta[21];
        isUnblockCHV2Blocked = (puk2Status & 0xF)==0;




        
    }
    
    public String getCHVInfo()
    {
        StringBuilder sb = new StringBuilder();
                if( isCHV1Enabled )
            sb.append("CHV1 enabled");
        else
            sb.append("CHV1 disabled");
        
        byte numberCHV1CHV2ADM = this.resposta[16];
        sb.append("\nNumber of CHVs, UNBLOCK CHV, and administrative codes: " + numberCHV1CHV2ADM);
        
        byte chv1Status = this.resposta[18];
        sb.append("\nCHV1 status-> Number of false presentations remaining ('0' means blocked): " + (chv1Status & 0xF));
        sb.append("\nCHV1 status-> ");
        sb.append((byte)(0x80 & chv1Status) == (byte)0x80?"secret code initialised":"secret code not initialised");
        
        byte puk1Status = this.resposta[19];
        sb.append("\nUNBLOCK CHV1 status-> Number of false presentations remaining ('0' means blocked): " + (puk1Status & 0xF));
        sb.append("\nUNBLOCK CHV1 status-> ");
        sb.append((byte)(0x80 & puk1Status) == (byte)0x80?"secret code initialised":"secret code not initialised");
        

        byte chv2Status = this.resposta[20];
        sb.append("\nCHV2 status-> Number of false presentations remaining ('0' means blocked): " + (chv2Status & 0xF));
        sb.append("\nCHV2 status-> ");
        sb.append((byte)(0x80 & chv2Status) == (byte)0x80?"secret code initialised":"secret code not initialised");
        
        byte puk2Status = this.resposta[21];
        sb.append("\nUNBLOCK CHV2 status-> Number of false presentations remaining ('0' means blocked): " + (puk2Status & 0xF));
        sb.append("\nUNBLOCK CHV2 status-> ");
        sb.append((byte)(0x80 & puk2Status) == (byte)0x80?"secret code initialised":"secret code not initialised");
        
        
        return sb.toString();

        
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

        sb.append("\nType of file: " + typeOfFile);
        
        sb.append("\n").append(this.getCHVInfo());
        
        return sb.toString();
        
        
    }
    
}
