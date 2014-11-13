package org.dynjs.runtime.builtins.types.regexp;

import static org.joni.constants.MetaChar.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

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
import org.joni.exception.JOniException;

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

    public void setPatternAndFlags(ExecutionContext context, String pattern, final String flags) {
        checkSyntaxOfFlags(context, flags);

        // 15.10.4.1:
        // If P is the empty String, this specification can be met by letting S be "(?:)".
        if (pattern.equals("")) {
            pattern = "(?:)";
        }

        defineOwnProperty(null, "source",
                PropertyDescriptor.newDataPropertyDescriptor(pattern, false, false, false), false);

        if (flags != null) {
            defineOwnProperty(null, "multiline",
                    PropertyDescriptor.newDataPropertyDescriptor(flags.contains("m"), false, false, false), false);
            defineOwnProperty(null, "global",
                    PropertyDescriptor.newDataPropertyDescriptor(flags.contains("g"), false, false, false), false);
            defineOwnProperty(null, "ignoreCase",
                    PropertyDescriptor.newDataPropertyDescriptor(flags.contains("i"), false, false, false), false);
        } else {
            defineOwnProperty(null, "multiline",
                    PropertyDescriptor.newDataPropertyDescriptor(false, false, false, false), false);
            defineOwnProperty(null, "global",
                    PropertyDescriptor.newDataPropertyDescriptor(false, false, false, false), false);
            defineOwnProperty(null, "ignoreCase",
                    PropertyDescriptor.newDataPropertyDescriptor(false, false, false, false), false);
        }
        defineOwnProperty(null, "lastIndex",
                PropertyDescriptor.newDataPropertyDescriptor(0L, true, false, false), false);

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
            // We can't use pattern.getBytes("UTF-8") here because any
            // malformed input will get mapped to the ? character which
            // screws up the regexp
            Charset charset= Charset.forName("UTF-8");
            CharsetEncoder encoder = charset.newEncoder();
            encoder.onMalformedInput(CodingErrorAction.REPLACE);
            encoder.replaceWith(new byte[] { (byte) 1 });
            ByteBuffer patternBuffer = encoder.encode(CharBuffer.wrap(pattern));
            byte[] patternBytes = new byte[patternBuffer.limit()];
            patternBuffer.get(patternBytes, 0, patternBytes.length);
            this.pattern = new Regex(patternBytes, 0, patternBytes.length, flagsInt, UTF8Encoding.INSTANCE, Syntax.ECMAScript, new WarnCallback() {
                @Override
                public void warn(String message) {
                    System.err.println("WARN: " + message);
                }
            });

        } catch (JOniException e) {
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        } catch (CharacterCodingException e) {
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

    public Region match(ExecutionContext context, String str, int from) {
        byte[] strBytes;
        try {
            strBytes = str.getBytes("UTF-8");
        } catch (Exception e) {
            // Should not happen, really
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        }

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
