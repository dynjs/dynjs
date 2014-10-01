package org.dynjs.runtime.builtins.types.error;

import org.dynjs.runtime.*;

public class StackElement {

    public StackElement(String debugContext, ExecutionContext context) {
        this.debugContext = debugContext;
        this.context = context;

        this.contextString = "<global>";
        int dotLoc = this.debugContext.indexOf(".");
        if (dotLoc > 0) {
            this.contextString = this.debugContext.substring(0, dotLoc);
            this.function = this.debugContext.substring(dotLoc + 1);
        } else {
            this.function = this.debugContext;
        }
        this.lineNumber = context.getLineNumber();
    }

    public StackTraceElement toStackTraceElement() {
        return new StackTraceElement(contextString, function, context.getFileName(), lineNumber);
    }

    public String toString() {
        return this.debugContext + " (" + context.getFileName() + ":" + lineNumber + ":" + getColumnNumber() + ")";
    }

    public String getFileName() {
        return context.getFileName();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getDebugContext() {
        return debugContext;
    }

    public Object getFunction() {
        return context.getFunctionReference();
    }

    public String getFunctionName() {
        return function;
    }

    public Object getThis() {
        return context.getThisBinding();
    }

    public Object getTypeName() {
        Object thisBinding = getThis();
        if (thisBinding instanceof JSObject) {
            JSObject ctor = (JSObject) ((JSObject) thisBinding).get(null, "constructor");
            if (ctor != null) {
                if (ctor.get(null, "name") != Types.UNDEFINED) {
                    return ctor.get(null, "name");
                } else {
                    return ctor.getClassName();
                }
            }
        }
        return null;
    }

    public Object getMethodName() {
        if (getFunction() != null) {
            Reference functionBinding = (Reference) getFunction();
            return functionBinding.getReferencedName();
        }
        return null;
    }

    public int getColumnNumber() {
        return this.context.getColumnNumber();
    }

    public boolean isNative() {
        if (getFunction() != null) {
            return ((Reference)getFunction()).getValue(null) instanceof AbstractNativeFunction;
        }
        return false;
    }

    public boolean isTopLevel() {
        if (this.getThis() != null) {
            return this.getThis() instanceof GlobalContext;
        }
        return false;
    }

    public boolean isConstructor() {
        if (this.getFunction() != null) {
            JSFunction func = (JSFunction) ((Reference)getFunction()).getValue(null);
            return func.isConstructor();
        }
        return false;
    }

    public boolean isEval() {
        return this.context.inEval();
    }

    public StackElement getEvalOrigin() {
        if (isEval()) {
            // not 100% sure if this is kosher
            return this;
        }
        return null;
    }

    private final String debugContext;
    private final String function;
    private final int lineNumber;
    private final ExecutionContext context;
    private String contextString;
}
