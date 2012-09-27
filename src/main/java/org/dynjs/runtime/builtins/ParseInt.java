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

import org.dynjs.runtime.AbstractPrototypeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class ParseInt extends AbstractPrototypeFunction {

    public ParseInt(GlobalObject globalObject) {
        super(globalObject, "text", "radix");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        String text = Types.toString(context, arguments[0]).trim();
        Object radixArg = arguments[1];

        long radix = 10;
        if (radixArg != Types.UNDEFINED) {
            radix = Types.toInt32(context, radixArg);
            if (radix == 0) { radix = 10; }
        } else {
            radix = extractRadix(text);
        }
        text = cleanText(text, radix);
        return parseInt(text, radix);
  }

    static String cleanText(String text, long radix) {
        if (radix == 16) {
            if (text.startsWith("0x") || text.startsWith("0X")) {
                return text.substring(2);
            }
        }
        // Java considers Unicode non-breaking space
        // to be a non-whitespace character.
        // Silly Java
        text = text.replaceAll("\\p{javaSpaceChar}", "").trim();
        /**
         * If S contains any character that is not a radix-R digit,
         * then let Z be the substring of S consisting of all characters before
         * the first such character; otherwise, let Z be S
         */
        String regex = "[^-.0-" + (radix-1) + "].*";
        text = text.replaceAll(regex, "");
        return text;
    }

    static int extractRadix(String text) {
        int radix = 10;
        if (text.startsWith("0x")) {
            radix = 16;
        }
        return radix;
    }

    static Object parseInt(String text, long radix) {
        int dotLoc = text.indexOf('.');
        if (dotLoc >= 0) {
            text = text.substring(0, dotLoc);
        }

        try {
            return Integer.parseInt(text, (int) radix);
        } catch (NumberFormatException e) {
            // ignore
        }
        return Double.NaN;
    }

}
