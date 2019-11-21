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

/**
 *
 * @author Gustavo Vieira Rocha Rabelo <gustavo.vrr@gmail.com>
 */
public class FileMap {

    static private Object[] map = new Object[]{
        
        
        "MF",0x3f00,
        "ICCID",0x2fe2,
        "TELECOM",0x7f10,
        "ADN",0x6f3a,
        "FDN",0x6f3b,
        "SMS",0x6f3c,
        "CCP",0x6f3d,
        "MSISDN",0x6f40,
        "SMSP",0x6f42,
        "SMSS",0x6f43,
        "LND",0x6f44,
        "SDN",0x6f49,
        "EXT1",0x6f4a,
        "EXT2",0x6f4b,
        "EXT3",0x6f4c,
        "GSM",0x7f20,
        "LP",0x6f05,
        "IMSI",0x6f07,
        "KC",0x6f20,
        "PLMNSel",0x6f30,
        "HPLMN",0x6f31,
        "ACMmax",0x6f37,
        "SST",0x6f38,
        "ACM",0x6f39,
        "GID1",0x6f3e,
        "GID2",0x6f3f,
        "PUCT",0x6f41,
        "CBMI",0x6f45,
        "SPN",0x6f46,
        "CBMID",0x6f48,
        "BCCH",0x6f74,
        "ACC",0x6f78,
        "FPLMN",0x6f7b,
        "LOCI",0x6f7e,
        "AD",0x6fad,
        "PHASE",0x6fae,
        "VGCS",0x6fb1,
        "VGCSS",0x6fb2,
        "VBS",0x6fb3,
        "VBSS",0x6fb4,
        "eMLPP",0x6fb5,
        "AAeM",0x6fb6,
        "ECC",0x6fb7,
        "CBMIR",0x6f50,
        "DF.ADMIN",0x7f4d,
        "EF.OPN",0x8f0c,
        "EF 8f 0d", 0x8f0d,
        "EF 8f 0e", 0x8f0e
          
        };
    
    
    static byte[] nameToHex(String name)
    {
        for(int i=0; i<map.length/2;i++)
            if(name.equals(map[i*2]))
            {
                byte b1, b2;
                int r = (int)map[i*2+1];
                b1 = (byte)((r >> 8) & 0xFF);
                b2 = (byte)(r & 0xFF);
                return new byte[]{b1, b2};
            }
        return null;
    }
    
}
