package org.dynjs.exception;

import org.dynjs.runtime.*;

import java.util.ArrayList;

public class ThrowException extends DynJSException {

    private static final long serialVersionUID = -5523478980527254739L;
    private final ExecutionContext context;

    private Object value;
    private ArrayList<StackElement> stack;

    public ThrowException(final ExecutionContext context, Throwable value) {
        super(value);
        this.value = value;
        this.context = context;
        setUpStackElements(context);
    }

    public ThrowException(final ExecutionContext context, Object value) {
        this.value = value;
        this.context = context;
        setUpStackElements(context);
        if ( value instanceof JSObject ) {
            PropertyDescriptor stackDesc = new PropertyDescriptor();
            stackDesc.setGetter(new JavaStackGetter(context.getGlobalObject(), this));
            ((JSObject) value).defineOwnProperty(context, "stack", stackDesc, false);
        }
    }

    protected void setUpStackElements(final ExecutionContext context) {
        this.stack = new ArrayList<StackElement>();
        context.collectStackElements(this.stack);
        int stackSize = this.stack.size();

        StackTraceElement[] javaElements = getStackTrace();

        StackTraceElement[] elements = new StackTraceElement[javaElements.length + stackSize];
        for (int i = 0; i < stackSize; ++i) {
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
            elements[i + stackSize] = javaElements[i];
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

    @Override
    public synchronized Throwable getCause() {
        Throwable cause = super.getCause();
        if ( cause != null ) {
            return cause;
        }

        if ( this.value instanceof JSObject ) {
            Object jsCause = ((JSObject) this.value).get( this.context, "cause" );
            if ( jsCause instanceof Throwable ) {
                return (Throwable) jsCause;
            }
            return new ThrowException( this.context, jsCause );
        }

        return null;
    }

    public String toString() {
        return "[ThrowException: value=" + this.value + "; cause=" + this.getCause() + "]";
    }
}
