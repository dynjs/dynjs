package org.dynjs.runtime.builtins.types.regexp;

import java.io.UnsupportedEncodingException;
import java.util.regex.PatternSyntaxException;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;
import org.joni.Syntax;
import org.joni.exception.ValueException;

public class DynRegExp extends DynObject {

    private Regex pattern;

    public DynRegExp(GlobalObject globalObject) {
        super(globalObject);
        setClassName("RegExp");
        setPrototype(globalObject.getPrototypeFor("RegExp"));
    }

    public DynRegExp(GlobalObject globalObject, final String pattern, final String flags) {
        this(globalObject);
        setPatternAndFlags(null, pattern, flags);
    }

    public void setPatternAndFlags(ExecutionContext context, final String pattern, final String flags) {
        checkSyntaxOfFlags(context, flags);

        defineOwnProperty(null, "source", new PropertyDescriptor() {
            {
                set("Value", pattern);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);

        if (flags != null) {
            defineOwnProperty(null, "multiline", new PropertyDescriptor() {
                {
                    set("Value", flags.contains("m"));
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
            defineOwnProperty(null, "global", new PropertyDescriptor() {
                {
                    set("Value", flags.contains("g"));
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
            defineOwnProperty(null, "ignoreCase", new PropertyDescriptor() {
                {
                    set("Value", flags.contains("i"));
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
        } else {
            defineOwnProperty(null, "multiline", new PropertyDescriptor() {
                {
                    set("Value", false);
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
            defineOwnProperty(null, "global", new PropertyDescriptor() {
                {
                    set("Value", false);
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
            defineOwnProperty(null, "ignoreCase", new PropertyDescriptor() {
                {
                    set("Value", false);
                    set("Writable", false);
                    set("Configurable", false);
                    set("Enumerable", false);
                }
            }, false);
        }
        defineOwnProperty(null, "lastIndex", new PropertyDescriptor() {
            {
                set("Value", 0L);
                set("Writable", true);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);

        int flagsInt = 0;

        if (get(context, "multiline") == Boolean.TRUE) {
            flagsInt = flagsInt | Option.MULTILINE;
        }

        if (get(context, "ignoreCase") == Boolean.TRUE) {
            flagsInt = flagsInt | Option.IGNORECASE;
        }
        try {
            byte[] patternBytes = pattern.getBytes("UTF-8");
            this.pattern = new Regex(patternBytes, 0, patternBytes.length, flagsInt, UTF8Encoding.INSTANCE, Syntax.RUBY );
        } catch (ValueException e) {
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        } catch (UnsupportedEncodingException e) {
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        }
    }

    private void checkSyntaxOfFlags(ExecutionContext context, String flags) {
        if (flags == null || flags.equals("")) {
            return;
        }

        for (int i = 0; i < flags.length(); ++i) {
            switch (flags.charAt(i)) {
            case 'm':
            case 'i':
            case 'g':
                break;
            default:
                throw new ThrowException(context, context.createSyntaxError("invalid flag '" + flags.charAt(i) + "'"));
            }
        }

        int index = -1;

        index = flags.indexOf('m');
        if (index >= 0) {
            if (flags.indexOf('m', index + 1) >= 0) {
                throw new ThrowException(context, context.createSyntaxError("'m' flag specified more than once"));
            }
        }

        index = flags.indexOf('i');
        if (index >= 0) {
            if (flags.indexOf('i', index + 1) >= 0) {
                throw new ThrowException(context, context.createSyntaxError("'i' flag specified more than once"));
            }
        }

        index = flags.indexOf('g');
        if (index >= 0) {
            if (flags.indexOf('g', index + 1) >= 0) {
                throw new ThrowException(context, context.createSyntaxError("'g' flag specified more than once"));
            }
        }
    }

    public Region match(String str, int from) {
        byte[] strBytes = str.getBytes();
        Matcher matcher = this.pattern.matcher(strBytes, 0, strBytes.length);
        if ( matcher.search(from, strBytes.length, 0) >= 0 ) {
            return matcher.getEagerRegion();
        }
        return null;
    }
}
