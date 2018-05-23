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

import java.util.ArrayList;
import simexplorer.apdusender.APDUSender;
import simexplorer.files.EF;
import simexplorer.decoders.DecodeSMS;
import simexplorer.decoders.SIMFileNotFoundException;

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class SMSCollection {
    
    
    private ArrayList<String> numeros = new ArrayList<>();
    private ArrayList<String> nomes = new ArrayList<>();
    private ArrayList<String> estados = new ArrayList<>();
    private ArrayList<String> pastas = new ArrayList<>();
    private ArrayList<String> tipos = new ArrayList<>();
    private ArrayList<String> textos = new ArrayList<>();
    private ArrayList<String> smscs = new ArrayList<>();
    private ArrayList<Boolean> registrosApagados = new ArrayList<>();
    private ArrayList<String> datahora = new ArrayList<>();
    
    public boolean getRegistroApagado(int i)
    {
        return registrosApagados.get(i);
    }
    
    public String getEstado(int i) {
        return estados.get(i);
    }

    public String getNumero(int i) {
        return numeros.get(i);
    }

    public String getNome(int i) {
        return nomes.get(i);
    }

    public String getPasta(int i) {
        return pastas.get(i);
    }

    public String getTipo(int i) {
        return tipos.get(i);
    }

    public String getTexto(int i) {
        return textos.get(i);
    }

    public String getSmsc(int i) {
        return smscs.get(i);
    }
    
    public String getDataHora(int i)
    {
        return datahora.get(i);
    }
    
    
    
    
    private final EF ef;
    public SMSCollection(APDUSender apduSender, ADNCollection adnCollection) throws SIMFileNotFoundException
    {
            ef = new EF(apduSender, "SMS", new String[]{"MF", "TELECOM"});
            
            for(int i=0; i<ef.getNumRegs(); i++)
                {
                    DecodeSMS decodeSMS = new DecodeSMS();
                    try
                    {
                        decodeSMS.decode(ef.getContentsLinearCyclic(i));
                    
                        if(decodeSMS.getRegistroValido() || decodeSMS.getRegistroApagado())
                        {
                            nomes.add(adnCollection.getNomeByNumero(decodeSMS.getNumero())  );
                            numeros.add(decodeSMS.getNumero());
                            estados.add(decodeSMS.getEstado());
                            pastas.add(decodeSMS.getPasta());
                            tipos.add(decodeSMS.getTipo());
                            textos.add(decodeSMS.getTexto());
                            smscs.add(decodeSMS.getSmsc());
                            registrosApagados.add(decodeSMS.getRegistroApagado());
                            datahora.add(decodeSMS.getDataHora());

                        }
                    }
                    catch(Exception  ex)
                    {
                        
                    }
                }
            
    }
    
    public int getNumRegs()
    {
        return nomes.size();
    }
    

    

}
