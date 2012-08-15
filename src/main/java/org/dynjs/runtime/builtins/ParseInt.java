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

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class ParseInt extends AbstractNativeFunction {

    public ParseInt(GlobalObject globalObject) {
        super( globalObject, "text", "radix" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        String text = ((String) arguments[0]).trim();
        Object radixArg = arguments[1];

        int radix = 10;

        if (radixArg != Types.UNDEFINED) {
            radix = ((Number) radixArg).intValue();
        } else {
            if (text.startsWith( "0x" )) {
                text = text.substring( 2 );
                radix = 16;
            } else if (text.startsWith( "0" )) {
                text = text.substring( 1 );
                radix = 8;
            }
        }
        
        if ( radix == 16 ) {
            if ( text.startsWith( "0x" ) || text.startsWith( "0X"  ) ) {
                text = text.substring(2);
            }
        } else if ( radix == 0 ) {
            radix = 10;
        }

        int dotLoc = text.indexOf( '.' );
        if (dotLoc >= 0) {
            text = text.substring( 0, dotLoc );
        }

        try {
            System.err.println( "text: " + text );
            System.err.println( "radix: " + radix );
            return Integer.parseInt( text, radix );
        } catch (NumberFormatException e) {

        }
        return Double.NaN;
    }

}
