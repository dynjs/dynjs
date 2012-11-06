package org.dynjs.runtime.builtins;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;

public class URLCodec {
    
    public static String URI_RESERVED_SET = ";/?:@&=+$,";

    public static String decode(ExecutionContext context, String str, String reservedSet) {

        int len = str.length();
        StringBuffer r = new StringBuffer();

        int k = 0;

        while (true) {
            String s = null;
            if (k == len) {
                return r.toString();
            }

            char c = str.charAt(k);

            if (c != '%') {
                s = "" + c;
            } else {
                int start = k;
                if ((k + 2) >= len) {
                    throw new ThrowException(context, context.createUriError("invalid escape"));
                }
                if (!isHexDigit(str.charAt(k + 1)) || !isHexDigit(str.charAt(k + 2))) {
                    throw new ThrowException(context, context.createUriError("invalid escape"));
                }
                int b = Integer.parseInt(str.substring(k + 1, k + 3), 16);
                k = k + 2;

                if ((b & 0x80) == 0) {
                    if (!reservedSet.contains("" + b)) {
                        s = "" + b;
                    } else {
                        s = str.substring(start, k + 1);
                    }
                } else {
                    int n = 0;
                    for (int nPos = 0; nPos < 8; ++nPos) {
                        if (((b << nPos) & 0x80) == 0) {
                            n = nPos;
                            break;
                        }
                    }

                    if (n == 1 || n > 4) {
                        throw new ThrowException(context, context.createUriError("invalid escape"));
                    }

                    int[] octets = new int[n];
                    octets[0] = b;

                    if ((k + (3 * (n - 1))) >= len) {
                        throw new ThrowException(context, context.createUriError("invalid escape"));
                    }

                    for (int j = 1; j < n; ++j) {
                        ++k;
                        if (str.charAt(k) != '%') {
                            throw new ThrowException(context, context.createUriError("invalid escape"));
                        }
                        if (!isHexDigit(str.charAt(k + 1)) || !isHexDigit(str.charAt(k + 2))) {
                            throw new ThrowException(context, context.createUriError("invalid escape"));
                        }
                        b = Integer.parseInt(str.substring(k + 1, k + 3), 16);

                        if (((b & 0x80) != 0) || ((b ^ 0x40) != 1)) {
                            throw new ThrowException(context, context.createUriError("invalid escape"));
                        }
                        k = k + 2;
                        octets[j] = b;
                    }

                    int v = 0;

                    for (int i = 0; i < octets.length; ++i) {
                        if (i == 0) {
                            v = octets[i];
                        } else {
                            v = v << 1;
                            v = v & octets[i];
                        }
                    }

                    if (!Character.isValidCodePoint(v)) {
                        throw new ThrowException(context, context.createUriError("invalid code-point: " + v));
                    }

                    if (v < 0x10000) {
                        if (!reservedSet.contains("" + v)) {
                            s = "" + b;
                        } else {
                            s = str.substring(start, k + 1);
                        }
                    } else {
                        char l = (char) (((v - 0x10000) & 0x3FF) + 0xDC00);
                        char h = (char) ((((v - 0x10000) >> 10) & 0x3FF) + 0xD800);
                        s = new String(new char[] { l, h });
                    }
                }

            }

            r.append(s);
        }

    }

    protected static boolean isHexDigit(char c) {
        return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'));
    }

}
