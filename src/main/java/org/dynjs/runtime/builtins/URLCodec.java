package org.dynjs.runtime.builtins;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;

public class URLCodec {

    public static String URI_RESERVED_SET = ";/?:@&=+$,";
    public static String URI_ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String DECIMAL_DIGIT = "0123456789";
    public static String URI_MARK = "-_.!~*'()";
    public static String URI_UNESCAPED_SET = URI_ALPHA + DECIMAL_DIGIT + URI_MARK;

    public static String encode(ExecutionContext context, String str, String unescapedSet) {
        int len = str.length();

        StringBuilder r = new StringBuilder();

        int k = 0;

        while (true) {
            if (k == len) {
                return r.toString();
            }

            char c = str.charAt(k);

            if (unescapedSet.contains("" + c)) {
                r.append(c);
            } else {

                if ((!(c < 0xDC00)) && (!(c > 0xDFFF))) {
                    throw new ThrowException(context, context.createUriError("invalid escape"));
                }

                long v = 0;

                if (c < 0xD800 || c > 0xDBFF) {
                    v = c;
                } else {
                    ++k;
                    if (k == len) {
                        throw new ThrowException(context, context.createUriError("invalid escape"));
                    }

                    char kChar = str.charAt(k);

                    if (kChar < 0xDC00 || kChar > 0xDFFF) {
                        throw new ThrowException(context, context.createUriError("invalid escape"));
                    }

                    v = ((c - 0xD800) * 0x400 + (kChar - 0xDC00) + 0x10000);
                }

                if (v < 0x80) {
                    r.append(String.format("%%%02X", v));
                } else if (v < 0x800) {
                    int o1 = (int) ((v >> 6) | 0xC0);
                    int o2 = (int) ((v & 0x3F) | 0x80);
                    r.append(String.format("%%%02X%%%02X", o1, o2));
                } else if (v <= 0xFFFF) {
                    int o1 = (int) (((v >> 12) & 0x1F) | 0xE0);
                    int o2 = (int) (((v >> 6) & 0x3F) | 0x80);
                    int o3 = (int) ((v & 0x3F) | 0x80);
                    r.append(String.format("%%%02X%%%02X%%%02X", o1, o2, o3));
                } else if (v <= 0x10FFFF) {
                    int o1 = (int) (((v >> 18) & 0x07) | 0xF0);
                    int o2 = (int) (((v >> 12) & 0x3F) | 0x80);
                    int o3 = (int) (((v >> 6) & 0x3F) | 0x80);
                    int o4 = (int) ((v & 0x3F) | 0x80);
                    r.append(String.format("%%%02X%%%02X%%%02X%%%02X", o1, o2, o3, o4));
                }
            }

            ++k;
        }
    }

    public static String decode(ExecutionContext context, String str, String reservedSet) {

        int len = str.length();
        StringBuilder r = new StringBuilder();

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
                    throw new ThrowException(context, context.createUriError("invalid escape (not enough chars follow %)"));
                }
                if (!isHexDigit(str.charAt(k + 1)) || !isHexDigit(str.charAt(k + 2))) {
                    throw new ThrowException(context, context.createUriError("invalid escape (non-hex follow %)"));
                }
                int b = Integer.parseInt(str.substring(k + 1, k + 3), 16);
                k = k + 2;

                if ((b & 0x80) == 0) {
                    String charStr = new String(new char[] { (char) b });

                    if (!reservedSet.contains(charStr)) {
                        s = charStr;
                    } else {
                        s = str.substring(start, k + 1);
                    }
                } else {
                    int n = 1;
                    for (int nPos = 1; nPos < 8; ++nPos) {
                        if (((b << nPos) & 0x80) == 0) {
                            n = nPos;
                            break;
                        }
                    }

                    if (n == 1 || n > 4) {
                        throw new ThrowException(context, context.createUriError("invalid escape (too many hex sequences)"));
                    }

                    int[] octets = new int[n];
                    octets[0] = b;

                    if ((k + (3 * (n - 1))) >= len) {
                        throw new ThrowException(context, context.createUriError("invalid escape (too many hex sequences)"));
                    }

                    for (int j = 1; j < n; ++j) {
                        ++k;
                        if (str.charAt(k) != '%') {
                            throw new ThrowException(context, context.createUriError("invalid escape (multiple hex sequences expected)"));
                        }
                        if (!isHexDigit(str.charAt(k + 1)) || !isHexDigit(str.charAt(k + 2))) {
                            throw new ThrowException(context, context.createUriError("invalid escape (following sequences do not contain hex sequences)"));
                        }
                        b = Integer.parseInt(str.substring(k + 1, k + 3), 16);

                        if ((b & 0x80) == 0) {
                            throw new ThrowException(context, context.createUriError("invalid escape (first significant bit must be 1)"));
                        }
                        if ((b & 0x40) != 0) {
                            throw new ThrowException(context, context.createUriError("invalid escape (second significant bit must be 0)"));
                        }
                        k = k + 2;
                        octets[j] = b;
                    }

                    int v = 0;

                    switch (octets.length) {
                    case 1:
                        v = octets[0] & 0x7F;
                        break;
                    case 2:
                        v = octets[0] & 0x3F;
                        break;
                    case 3:
                        v = octets[0] & 0x1F;
                        break;
                    case 4:
                        v = octets[0] & 0x0F;
                        break;
                    }

                    for (int i = 1; i < octets.length; ++i) {
                        v = v << 6;
                        v = v | (octets[i] & 0x3F);
                    }

                    if (!Character.isValidCodePoint(v)) {
                        throw new ThrowException(context, context.createUriError("invalid code-point: " + v));
                    }

                    if (v < 0x10000) {
                        String charStr = new String(new char[] { (char) v });
                        if (!reservedSet.contains(charStr)) {
                            s = charStr;
                        } else {
                            s = str.substring(start, k + 1);
                        }
                    } else {
                        char l = (char) (((v - 0x10000) & 0x3FF) + 0xDC00);
                        char h = (char) ((((v - 0x10000) >> 10) & 0x3FF) + 0xD800);
                        s = new String(new char[] { h, l });
                    }
                }

            }

            r.append(s);
            ++k;
        }

    }

    protected static boolean isHexDigit(char c) {
        return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'));
    }

}
