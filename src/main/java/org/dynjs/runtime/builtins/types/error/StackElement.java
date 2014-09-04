package org.dynjs.runtime.builtins.types.error;

import org.dynjs.runtime.JSObject;

public class StackElement {

    public StackElement(String fileName, int lineNumber, String debugContext, Object functionReference, Object thisBinding) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.debugContext = debugContext;
        this.functionReference = functionReference;
        this.thisBinding = thisBinding;

        this.context = "<global>";
        int dotLoc = this.debugContext.indexOf(".");
        if (dotLoc > 0) {
            this.context = this.debugContext.substring(0, dotLoc);
            this.function = this.debugContext.substring(dotLoc + 1);
        } else {
            this.function = this.debugContext;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getDebugContext() {
        return debugContext;
    }

    public Object getFunction() {
        return functionReference;
    }

    public String getFunctionName() {
        return function;
    }

    public Object getThis() {
        return thisBinding;
    }

    public String getTypeName() {
        if (thisBinding instanceof JSObject) {
            System.err.println("CTOR: " + ((JSObject)thisBinding).getOwnProperty(null, "constructor"));
        }
        return null;
    }

    /*
    We should ultimately support all of these additional properties on a StackElement
    getTypeName: returns the type of this as a string. This is the name of the function stored in the constructor field of this, if available, otherwise the object's [[Class]] internal property.
    getMethodName: returns the name of the property of this or one of its prototypes that holds the current function
    getColumnNumber: if this function was defined in a script returns the current column number
    getEvalOrigin: if this function was created using a call to eval returns a CallSite object representing the location where eval was called
    isToplevel: is this a toplevel invocation, that is, is this the global object?
    isEval: does this call take place in code defined by a call to eval?
    isNative: is this call in native V8 code?
    isConstructor: is this a constructor call?
     */
    public StackTraceElement toStackTraceElement() {
        return new StackTraceElement(context, function, this.fileName, this.lineNumber);
    }

    public String toString() {
        return this.debugContext + " (" + this.fileName + ":" + this.lineNumber + ")";
    }

    private String fileName;
    private int lineNumber;
    private String debugContext;
    private String function;
    private String context;
    private final Object thisBinding;
    private final Object functionReference;

}
