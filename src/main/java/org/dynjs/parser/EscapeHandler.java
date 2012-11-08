package org.dynjs.parser;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;

public class EscapeHandler {

    private static final String VERTICAL_TAB = new String(new char[] { 11 });

    public EscapeHandler() {

    }

    public String unescape(ExecutionContext context, String in, boolean strict) {
        StringBuffer out = new StringBuffer();

        int len = in.length();
        int cur = 0;

        while (cur < len) {
            int slashLoc = in.indexOf("\\", cur);

            if (slashLoc >= 0) {
                out.append(in.substring(cur, slashLoc));
                char c = in.charAt(slashLoc + 1);

                switch (c) {
                case '\\':
                    out.append("\\");
                    cur = slashLoc + 2;
                    break;
                case 'b':
                    out.append("\b");
                    cur = slashLoc + 2;
                    break;
                case 'f':
                    out.append("\f");
                    cur = slashLoc + 2;
                    break;
                case 'n':
                    out.append("\n");
                    cur = slashLoc + 2;
                    break;
                case 'r':
                    out.append("\r");
                    cur = slashLoc + 2;
                    break;
                case 't':
                    out.append("\t");
                    cur = slashLoc + 2;
                    break;
                case 'v':
                    out.append(VERTICAL_TAB);
                    cur = slashLoc + 2;
                    break;
                case 'x':
                    out.append(handleHexEscape(in.substring(slashLoc + 2, slashLoc + 4)));
                    cur = slashLoc + 4;
                    break;
                case 'u':
                    out.append(handleUnicodeEscape(in.substring(slashLoc + 2, slashLoc + 6)));
                    cur = slashLoc + 6;
                    break;
                case '0':
                    if (in.length() > slashLoc + 2) {
                        char followChar = in.charAt(slashLoc + 2);
                        if (!(followChar >= 0 && followChar <= 9)) {
                            out.append(new String(new char[] { 0 }));
                            cur = slashLoc + 2;
                            break;
                        }
                    } else {
                        out.append(new String(new char[] { 0 }));
                        cur = slashLoc + 2;
                        break;
                    }
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (strict) {
                        throw new ThrowException(context, context.createSyntaxError("octal not allowed in strict mode"));
                    }
                default:
                    cur = slashLoc + 1;
                }

            } else {
                break;
            }
        }

        out.append(in.substring(cur));

        return out.toString();
    }

    public String handleHexEscape(String hexStr) {
        int code = Integer.decode("0x" + hexStr);
        return Character.toString((char) code);
    }

    public String handleUnicodeEscape(String hexStr) {
        int code = Integer.decode("0x" + hexStr);
        return new String(Character.toChars(code));
    }

}
