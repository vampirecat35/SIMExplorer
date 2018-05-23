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
public abstract class File {

    protected byte[] resposta;
    final protected byte[] fileID;
    final protected TypeOfFile typeOfFile;
    final protected String nome;
    final protected String[] pais;
   
    private Boolean naoEncontrado(byte[] resposta)
    {
        return (resposta.length == 2) && resposta[0] == (byte)0x94 && resposta[1] == (byte)0x04;
    }
    
    public File(APDUSender apduSender, String nome, String[] pais) throws SIMFileNotFoundException 
    {
        this.nome = nome;
        this.pais = pais;
        fileID = FileMap.nameToHex(nome);
        if(fileID == null) throw (new SIMFileNotFoundException("File " + nome + " not mapped"));
        for(int i=0;i<pais.length;i++)
        {
            byte[] file = FileMap.nameToHex(pais[i]);
            if(file == null) throw (new SIMFileNotFoundException("File " + pais[i] + " not mapped"));
            resposta = apduSender.enviarAPDU(new byte[]{(byte)0xa0, (byte)0xa4, 0x00, 0x00, 0x02, file[0], file[1] });
            if(naoEncontrado(resposta)) throw (new SIMFileNotFoundException("File " + pais[i] + " not found."));
        }
        
        
        resposta = apduSender.enviarAPDU(new byte[]{(byte)0xa0, (byte)0xa4, 0x00, 0x00, 0x02, fileID[0], fileID[1] });
        if(naoEncontrado(resposta)) 
        {
            throw (new SIMFileNotFoundException("File " + nome + " not found."));
        }
        
        

        resposta = apduSender.enviarAPDU(new byte[]{(byte)0xa0, (byte)0xc0, 0x00, 0x00, resposta[1]});
        
        switch(resposta[6])
        {
            case 1: typeOfFile = TypeOfFile.MF; break;
            case 2: typeOfFile = TypeOfFile.DF; break;
            case 4: typeOfFile = TypeOfFile.EF; break;
            default: typeOfFile = TypeOfFile.INVALID;
        }
    
        

        
        
    }
    
    public byte[] getFileID(){
        return fileID;
    }
    
    public TypeOfFile getTypeOfFile(){
        return typeOfFile;
    }
    
    public String getNome()
    {
        return nome;
    }

    public String[] getPais() {
        return pais;
    }
    
    
}

