package org.dynjs.runtime.builtins.types.regexp;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;

public class DynRegExp extends DynObject {

    public DynRegExp(GlobalObject globalObject) {
        super( globalObject );
        setClassName("RegExp");
        setPrototype(globalObject.getPrototypeFor("RegExp"));
    }
    
    public DynRegExp(GlobalObject globalObject, final String pattern, final String flags) {
        this( globalObject );
        setPatternAndFlags(pattern, flags);
    }

    public void setPatternAndFlags(final String pattern, final String flags) {
        defineOwnProperty(null, "source", new PropertyDescriptor() {
            {
                set("Value", pattern);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
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
        defineOwnProperty(null, "lastIndex", new PropertyDescriptor() {
            {
                set("Value", 0);
                set("Writable", true);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
    }
}
