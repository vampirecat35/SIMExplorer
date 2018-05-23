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
public class DecodeSPN implements DecodeEF {

    private boolean displayOfRegisterdPLMNRequired;
    private String spn="";
    
    @Override
    public String decode(byte[] bytes) {
        byte dc = bytes[0];
        displayOfRegisterdPLMNRequired = ((dc & 1)==1);
        
        spn = "";
        for(int i=1; i<bytes.length; i++)
        {
            if(bytes[i] != (byte)0xFF)
                spn += (char)bytes[i];
        }
        
        return "Display of registered PLMN required = " + displayOfRegisterdPLMNRequired + "\nService Provider Name: " + spn;
        
        
    }
    
    public String getSPNName()
    {
        return spn;
    }
    

    
}
