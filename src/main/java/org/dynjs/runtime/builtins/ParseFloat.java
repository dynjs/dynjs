package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ParseFloat extends AbstractNonConstructorFunction {

    public ParseFloat(GlobalContext globalContext) {
        super(globalContext, "f");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String text = Types.toString(context, args[0]);

        if (text.equals("")) {
            return Double.NaN;
        }

        int len = text.length();
        int firstNonWhitespace = 0;

        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (Types.isWhitespace(c)) {
                // nothing
            } else {
                firstNonWhitespace = i;
                break;
            }
        }

        if (text.substring(firstNonWhitespace).startsWith("Infinity") || text.substring(firstNonWhitespace).startsWith("+Infinity")) {
            return Double.POSITIVE_INFINITY;
        }

        if (text.substring(firstNonWhitespace).startsWith("-Infinity")) {
            return Double.NEGATIVE_INFINITY;
        }

        int digitSearchStart = firstNonWhitespace;
        int lastDigit = firstNonWhitespace;

        if ((text.charAt(firstNonWhitespace) == '-') || (text.charAt(firstNonWhitespace) == '+')) {
            ++digitSearchStart;
        }

        boolean dotSeen = false;

        for (int i = digitSearchStart; i < len; ++i) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9') {
                lastDigit = i;
            } else {
                if (!dotSeen && c == '.') {
                    lastDigit = i;
                    dotSeen = true;
                } else {
                    break;
                }
            }
        }

        if (text.length() > (lastDigit + 1)) {
            digitSearchStart = lastDigit + 1;
            if (text.charAt(digitSearchStart) == 'e' || text.charAt(digitSearchStart) == 'E') {
                ++digitSearchStart;
                if ((text.charAt(digitSearchStart) == '-') || (text.charAt(digitSearchStart) == '+')) {
                    ++digitSearchStart;
                }
                for (int i = digitSearchStart; i < len; ++i) {
                    char c = text.charAt(i);
                    if (c >= '0' && c <= '9') {
                        lastDigit = i;
                    } else {
                        break;
                    }
                }
            }
        }

        //System.err.println( "in[" + text + "]" );
        text = text.substring(firstNonWhitespace, lastDigit + 1);
        //System.err.println( "out[" + text + "]" );

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/ParseFloat.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: parseFloat>";
    }
}
