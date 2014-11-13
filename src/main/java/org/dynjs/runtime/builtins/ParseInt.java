/**
 *  Copyright 2013 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime.builtins;

import java.math.BigInteger;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ParseInt extends AbstractNonConstructorFunction {
    
    public ParseInt(GlobalContext globalContext) {
        super(globalContext, "text", "radix");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        String inputString = Types.toString(context, arguments[0]);

        int len = inputString.length();
        int firstNonWhitespace = -1;

        for (int i = 0; i < len; ++i) {
            char c = inputString.charAt(i);
            if (Types.isWhitespace(c)) {
                // nothing
            } else {
                firstNonWhitespace = i;
                break;
            }
        }

        String s = null;
        int sign = 1;

        if (firstNonWhitespace < 0) {
            s = "";
        } else {
            s = inputString.substring(firstNonWhitespace);
            char c = s.charAt(0);
            if (c == '-') {
                sign = -1;
            }
            if (c == '-' || c == '+') {
                s = s.substring(1);
            }
        }
        

        long r = Types.toInt32(context, arguments[1]);

        boolean stripPrefix = true;

        if (r != 0) {
            if (r < 2 || r > 36) {
                return Double.NaN;
            }
            if (r != 16) {
                stripPrefix = false;
            }
        } else {
            r = 10;
        }

        if (stripPrefix) {
            if (s.startsWith("0X") || s.startsWith("0x")) {
                s = s.substring(2);
                r = 16;
            }
        }

        len = s.length();
        int firstInvalidDigit = -1;

        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (!isRadixDigit(c, r)) {
                firstInvalidDigit = i;
                break;
            }
        }

        String z = null;
        if (firstInvalidDigit < 0) {
            z = s;
        } else {
            z = s.substring(0, firstInvalidDigit);
        }

        if (z.equals("")) {
            return Double.NaN;
        }
        
        try {
            return sign * Long.parseLong(z, (int) r);
        } catch (NumberFormatException e) {
            return sign * new BigInteger(z, (int) r).doubleValue();
        }
    }

    static boolean isRadixDigit(char c, long radix) {
        if ( radix <= 10 ) {
            return ( c >= '0' && c <= ('0' + ( radix -1 )) );
        } else {
            return ( ( c >= '0' ) && ( c <= '9' ) || ( ( c >= 'a' ) && ( c <= ( 'a' + radix - 11) ) ) ) || ( ( c >= 'A' ) && ( c <= ( 'A' + radix - 11) ) );
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/ParseInt.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: parseInt>";
    }

}
