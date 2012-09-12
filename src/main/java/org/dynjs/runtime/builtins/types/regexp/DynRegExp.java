package org.dynjs.runtime.builtins.types.regexp;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;

public class DynRegExp extends DynObject {

    private Pattern pattern;

    public DynRegExp(GlobalObject globalObject) {
        super(globalObject);
        setClassName("RegExp");
        setPrototype(globalObject.getPrototypeFor("RegExp"));
    }

    public DynRegExp(GlobalObject globalObject, final String pattern, final String flags) {
        this(globalObject);
        setPatternAndFlags(pattern, flags);
    }

    public void setPatternAndFlags(final String pattern, final String flags) {
        this.pattern = Pattern.compile(pattern);
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
        }
        defineOwnProperty(null, "lastIndex", new PropertyDescriptor() {
            {
                set("Value", 0);
                set("Writable", true);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
    }

    public MatchResult match(String str, int from) {
        Matcher matcher = this.pattern.matcher(str);
        if (matcher.find(from)) {
            return matcher.toMatchResult();
        }
        return null;
    }
}
