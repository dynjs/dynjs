package org.dynjs.exception;

import java.util.ArrayList;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.StackElement;
import org.dynjs.runtime.StackGetter;
import org.dynjs.runtime.Types;

public class ThrowException extends DynJSException {

    private static final long serialVersionUID = -5523478980527254739L;
    
    private Object value;
    private ArrayList<StackElement> stack;

    public ThrowException(final ExecutionContext context, Throwable value) {
        super(value);
        this.value = value;
        setUpStackElements(context);
    }

    public ThrowException(final ExecutionContext context, Object value) {
        this.value = value;
        setUpStackElements(context);
    }

    protected void setUpStackElements(final ExecutionContext context) {
        this.stack = new ArrayList<StackElement>();
        context.collectStackElements(this.stack);

        StackTraceElement[] javaElements = getStackTrace();

        StackTraceElement[] elements = new StackTraceElement[javaElements.length + this.stack.size()];
        for (int i = 0; i < this.stack.size(); ++i) {
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
        for (int i = 0; i < javaElements.length; ++i) {
            elements[i + this.stack.size()] = javaElements[i];
        }
        setStackTrace(elements);
    }

    public String getMessage() {
        if (value instanceof JSObject) {
            String message = "";
            if (((JSObject) value).hasProperty(null, "name")) {
                message += ((JSObject) value).get(null, "name") + ": ";
            }
            if (((JSObject) value).hasProperty(null, "message")) {
                message += ((JSObject) value).get(null, "message");
            }
            return message;
        } else if ( value instanceof String ) {
            return value.toString();
        }
        return super.getMessage();
    }

    public Object getValue() {
        return this.value;
    }

}
