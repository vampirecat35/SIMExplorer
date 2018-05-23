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

/**
 *
 * @author Gustavo Rabelo <gustavo.vrr@gmail.com>
 */
public class AccessConditionsMap {
    static public AccessConditions getAcessConditions(byte b)
    {
        switch(b)
        {
            case 0: return AccessConditions.Always;
            case 1: return AccessConditions.CHV1;
            case 2: return AccessConditions.CHV2;
            case 4: return AccessConditions.ADM;
            case 0xE: return AccessConditions.ADM;
            case 0xF: return AccessConditions.NEV;
            default: return AccessConditions.ERROR;
        }
    }
}
