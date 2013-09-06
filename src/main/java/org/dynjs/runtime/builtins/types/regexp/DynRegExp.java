package org.dynjs.runtime.builtins.types.regexp;

import static org.joni.constants.MetaChar.*;

import java.io.UnsupportedEncodingException;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
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

        PropertyDescriptor sourceDesc = new PropertyDescriptor();
        sourceDesc.set(Names.VALUE, pattern);
        sourceDesc.set(Names.WRITABLE, false);
        sourceDesc.set(Names.CONFIGURABLE, false);
        sourceDesc.set(Names.ENUMERABLE, false);
        defineOwnProperty(null, "source", sourceDesc, false);

        if (flags != null) {
            PropertyDescriptor multilineDesc = new PropertyDescriptor();
            multilineDesc.set(Names.VALUE, flags.contains("m"));
            multilineDesc.set(Names.WRITABLE, false);
            multilineDesc.set(Names.CONFIGURABLE, false);
            multilineDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "multiline", multilineDesc, false);

            PropertyDescriptor globalDesc = new PropertyDescriptor();
            globalDesc.set(Names.VALUE, flags.contains("g"));
            globalDesc.set(Names.WRITABLE, false);
            globalDesc.set(Names.CONFIGURABLE, false);
            globalDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "global", globalDesc, false);

            PropertyDescriptor ignoreCaseDesc = new PropertyDescriptor();
            ignoreCaseDesc.set(Names.VALUE, flags.contains("i"));
            ignoreCaseDesc.set(Names.WRITABLE, false);
            ignoreCaseDesc.set(Names.CONFIGURABLE, false);
            ignoreCaseDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "ignoreCase", ignoreCaseDesc, false);
        } else {
            PropertyDescriptor multilineDesc = new PropertyDescriptor();
            multilineDesc.set(Names.VALUE, false);
            multilineDesc.set(Names.WRITABLE, false);
            multilineDesc.set(Names.CONFIGURABLE, false);
            multilineDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "multiline", multilineDesc, false);

            PropertyDescriptor globalDesc = new PropertyDescriptor();
            globalDesc.set(Names.VALUE, false);
            globalDesc.set(Names.WRITABLE, false);
            globalDesc.set(Names.CONFIGURABLE, false);
            globalDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "global", globalDesc, false);

            PropertyDescriptor ignoreCaseDesc = new PropertyDescriptor();
            ignoreCaseDesc.set(Names.VALUE, false);
            ignoreCaseDesc.set(Names.WRITABLE, false);
            ignoreCaseDesc.set(Names.CONFIGURABLE, false);
            ignoreCaseDesc.set(Names.ENUMERABLE, false);
            defineOwnProperty(null, "ignoreCase", ignoreCaseDesc, false);
        }
        PropertyDescriptor lastIndexDesc = new PropertyDescriptor();
        lastIndexDesc.set(Names.VALUE, 0L);
        lastIndexDesc.set(Names.WRITABLE, true);
        lastIndexDesc.set(Names.CONFIGURABLE, false);
        lastIndexDesc.set(Names.ENUMERABLE, false);
        defineOwnProperty(null, "lastIndex", lastIndexDesc, false);

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
            this.pattern = new Regex(patternBytes, 0, patternBytes.length, flagsInt, UTF8Encoding.INSTANCE, Syntax.JavaScript, new WarnCallback() {
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

    public boolean isGlobal() {
        return (boolean) this.get(null, "global");
    }
}
