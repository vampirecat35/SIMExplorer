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
public class DecodeLOCI implements DecodeEF{
    @Override
    public String decode(byte[] bytes) {

        String tmsi="";
        tmsi += Integer.toHexString(0xFF&bytes[0]).length()==2?Integer.toHexString(0xFF&bytes[0]).toUpperCase():"0" + Integer.toHexString(0xFF&bytes[0]).toUpperCase();
        tmsi += Integer.toHexString(0xFF&bytes[1]).length()==2?Integer.toHexString(0xFF&bytes[1]).toUpperCase():"0" + Integer.toHexString(0xFF&bytes[1]).toUpperCase();
        tmsi += Integer.toHexString(0xFF&bytes[2]).length()==2?Integer.toHexString(0xFF&bytes[2]).toUpperCase():"0" + Integer.toHexString(0xFF&bytes[2]).toUpperCase();
        tmsi += Integer.toHexString(0xFF&bytes[3]).length()==2?Integer.toHexString(0xFF&bytes[3]).toUpperCase():"0" + Integer.toHexString(0xFF&bytes[3]).toUpperCase();
        
        String mcc="";
        mcc += Integer.toHexString(bytes[4]&0xF);
        mcc += Integer.toHexString(((bytes[4])>>4)&0xF);
        mcc += Integer.toHexString(bytes[5]&0xF);
        
        
        return "TMSI: " + tmsi + "\n" + "MCC: " + mcc;
        
    }    
    
    
}
