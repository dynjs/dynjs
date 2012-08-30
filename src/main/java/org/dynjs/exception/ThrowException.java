package org.dynjs.exception;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.StackElement;
import org.dynjs.runtime.StackGetter;

public class ThrowException extends DynJSException {

    private Object value;
    private ArrayList<StackElement> stack;

    public ThrowException(Object value) {
        this.value = value;
    }

    public ThrowException(final ExecutionContext context, Object value) {
        this(value);
        this.stack = new ArrayList<StackElement>();
        context.collectStackElements(this.stack);

        StackTraceElement[] elements = new StackTraceElement[this.stack.size()];
        for (int i = 0; i < elements.length; ++i) {
            StackElement e = stack.get(i);
            String cn = "<global>";
            String fn = null;
            int dotLoc = e.debugContext.indexOf(".");
            if (dotLoc > 0) {
                cn = e.debugContext.substring(0, dotLoc);
                fn = e.debugContext.substring(dotLoc + 1);
            } else {
                fn = e.debugContext;
            }
            elements[i] = new StackTraceElement(cn, fn, e.fileName, e.lineNumber);
        }
        setStackTrace(elements);

        if (value instanceof JSObject) {
            String errorName = "<unknown>";
            if (((JSObject) value).hasProperty(context, "name")) {
                errorName = (String) ((JSObject) value).get(context, "name");
            }
            String message = null;
            if (((JSObject) value).hasProperty(context, "message")) {
                message = (String) ((JSObject) value).get(context, "message");
            }
            final String msg = message;
            final String err = errorName;
            ((JSObject) value).defineOwnProperty(context, "stack", new PropertyDescriptor() {
                {
                    set("Get", new StackGetter(context.getGlobalObject(), err, msg, stack));
                }
            }, false);
        }
    }

    public String getMessage() {
        if (value instanceof JSObject) {
            String message = "";
            if (((JSObject) value).hasProperty(null, "name")) {
                message += ((JSObject)value).get( null, "name" ) + ": ";
            }
            if (((JSObject) value).hasProperty(null, "message")) {
                message += ((JSObject) value).get(null, "message");
            }
            return message;
        }
        return super.getMessage();
    }

    public Object getValue() {
        return this.value;
    }

}
