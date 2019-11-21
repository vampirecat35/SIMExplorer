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

package simexplorer.reportgenerator;

import java.util.ArrayList;
import simexplorer.apdusender.APDUSender;
import simexplorer.files.EF;
import simexplorer.decoders.DecodeADN;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>
 */
public class ADNCollection {
    private final EF ef;
    private final ArrayList<String> nomes = new ArrayList<>();
    private final ArrayList<String> numeros = new ArrayList<>();
    public ADNCollection(APDUSender apduSender) throws SIMFileNotFoundException
    {
            ef = new EF(apduSender, "ADN", new String[]{"MF", "TELECOM"});
            
            for(int i=0; i<ef.getNumRegs(); i++)
                {
                    DecodeADN decodeADN = new DecodeADN();
                    decodeADN.decode(ef.getContentsLinearCyclic(i));
                    if(decodeADN.isRegistroValido())
                    {
                        nomes.add(decodeADN.getNome());
                        numeros.add(decodeADN.getNumero());
                    }
                }
            
    }
    
    public int getNumRegs()
    {
        return nomes.size();
    }
    
    public String getNome(int i)
    {
        return nomes.get(i);
    }
    
    public String getNumero(int i)
    {
        return numeros.get(i);
    }
    
    public String getNomeByNumero(String numero)
    {
        String num7 = "";
        String numero7 =  numero.substring(Math.max(0, numero.length() - 7));
        for(int i=0;i<this.getNumRegs();i++)
        {
            num7 = this.getNumero(i);
            num7 = num7.substring(Math.max(0, num7.length() - 7));
            if(num7.equals(numero7))
                return this.getNome(i);
        }
        
        return "";
    }
            
    

}
