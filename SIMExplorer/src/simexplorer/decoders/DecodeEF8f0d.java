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

/**
 *
 * @author Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>
 */
public class DecodeEF8f0d implements DecodeEF{

    @Override
    public String decode(byte[] bytes) throws IllegalArgumentException {
        

        //ki
        String ki="";
        for(int i=0;i<16;i++)
        {
            String hex = Integer.toHexString(0xFF&bytes[i]);
            if(hex.length()==1) hex = "0" + hex;
            ki += hex + " ";
        }
        
        //iccid
        String iccid="";
        for(int i=21;i<=30;i++)
        {
            String hex = Integer.toHexString(0xFF&bytes[i]);
            if(hex.length()==1) hex = "0" + hex;
            iccid += hex + " ";
        }
        
        //imsi
        String imsi="";
        for(int i=36;i<=44;i++)
        {
            String hex = Integer.toHexString(0xFF&bytes[i]);
            if(hex.length()==1) hex = "0" + hex;
            imsi += hex + " ";
        }
        
        return "ki = " + ki + "\niccid = " + iccid + "\nimsi = " + imsi;
        
        
        
    }
    
}
