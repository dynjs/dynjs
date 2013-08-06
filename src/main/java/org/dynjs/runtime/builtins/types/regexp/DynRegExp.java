package org.dynjs.runtime.builtins.types.regexp;

import static org.joni.constants.MetaChar.*;

import java.io.UnsupportedEncodingException;

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
import org.joni.WarnCallback;
import org.joni.Syntax.MetaCharTable;
import org.joni.constants.SyntaxProperties;
import org.joni.exception.JOniException;
import org.joni.exception.SyntaxException;

public class DynRegExp extends DynObject {

    public static final Syntax Javascript = new Syntax(
            ((Syntax.GNU_REGEX_OP
                    | Syntax.OP_QMARK_NON_GREEDY
                    | Syntax.OP_ESC_OCTAL3
                    | Syntax.OP_ESC_X_HEX2
                    //| Syntax.OP_ESC_X_BRACE_HEX8
                    | Syntax.OP_ESC_CONTROL_CHARS
                    | Syntax.OP_ESC_C_CONTROL
                    | Syntax.OP_DECIMAL_BACKREF
                    | Syntax.OP_ESC_D_DIGIT
                    | Syntax.OP_ESC_S_WHITE_SPACE
                    | Syntax.OP_ESC_W_WORD
            ) & ~Syntax.OP_ESC_LTGT_WORD_BEGIN_END),

            (Syntax.OP2_ESC_CAPITAL_Q_QUOTE
                    | Syntax.OP2_QMARK_GROUP_EFFECT
                    | Syntax.OP2_OPTION_PERL
                    | Syntax.OP2_ESC_P_BRACE_CHAR_PROPERTY
                    | Syntax.OP2_ESC_P_BRACE_CIRCUMFLEX_NOT
                    | Syntax.OP2_ESC_U_HEX4
                    | Syntax.OP2_ESC_V_VTAB
                    ),

            (Syntax.GNU_REGEX_BV
                    | Syntax.CONTEXT_INVALID_REPEAT_OPS
                    //| Syntax.STRICT_CHECK_BACKREF
            ),

            //Option.SINGLELINE,
            0,

            new MetaCharTable(
                    '\\', /* esc */
                    INEFFECTIVE_META_CHAR, /* anychar '.' */
                    INEFFECTIVE_META_CHAR, /* anytime '*' */
                    INEFFECTIVE_META_CHAR, /* zero or one time '?' */
                    INEFFECTIVE_META_CHAR, /* one or more time '+' */
                    INEFFECTIVE_META_CHAR /* anychar anytime */
            )
            );
    
    static {
    }

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
        } else {
            flagsInt = flagsInt | Option.SINGLELINE;
        }

        if (get(context, "ignoreCase") == Boolean.TRUE) {
            flagsInt = flagsInt | Option.IGNORECASE;
        }
        try {
            byte[] patternBytes = pattern.getBytes("UTF-8");
            this.pattern = new Regex(patternBytes, 0, patternBytes.length, flagsInt, UTF8Encoding.INSTANCE, Javascript, new WarnCallback() {
                @Override
                public void warn(String message) {
                    System.err.println("WARN: " + message);
                }
            });
            
        } catch (JOniException e) {
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
        if (matcher.search(from, strBytes.length, 0) >= 0) {
            return matcher.getEagerRegion();
        }
        return null;
    }

    public boolean isGlobal(){
        return (boolean) this.get(null, "global");
    }
}
