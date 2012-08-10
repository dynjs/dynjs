/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import org.dynjs.api.Function;

public class ParseInt implements Function {
    @Override
    public Object call(Object self, DynThreadContext context, Object... arguments) {
        return parseInt( arguments );
    }

    protected static Object parseInt(Object... arguments) {
        if (isDefinedCall( arguments )) {
            int radix = determineRadix( arguments );
            try {
                String value = arguments[0].toString().trim();
                if (isHexValue( value )) {
                    value = value.substring( 2 );
                }
                value = removeDecimal( value );
                return Integer.parseInt( value, radix );
            } catch (NumberFormatException e) {
                return Double.NaN;
            }
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public String[] getParameters() {
        return new String[] { "a", "b" }; // what is this for?
    }

    private static String removeDecimal(String value) {
        int i = value.indexOf( '.' );
        if (i == -1) {
            return value;
        }
        return value.substring( 0, i );
    }

    private static int determineRadix(Object[] arguments) {
        int radix = 10;
        if (arguments.length == 2) {
            radix = (int) Double.parseDouble( arguments[1].toString() );
            if (radix == 0) {
                radix = 10;
            }
        }
        if (radix < 2 || radix > 36) {
            radix = -1;
        }
        return radix;
    }

    private static boolean isHexValue(String string) {
        final String value = string.trim();
        return (value.startsWith( "0x" ) || value.startsWith( "0X" ));
    }

    private static boolean isDefinedCall(Object[] args) {
        boolean valid = true;
        if (args.length > 0 && args.length < 3) {
            if (args[0] == null) {
                valid = false;
            }
        } else {
            valid = false;
        }
        return valid;
    }
}
