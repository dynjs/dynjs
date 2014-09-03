package org.dynjs.runtime.builtins.types.error;

public class StackElement {

    public StackElement(String fileName, int lineNumber, String debugContext) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.debugContext = debugContext;
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

    public String getFunction() {
        return function;
    }
    /*
    We should ultimately support all of these properties on a StackElement
    getThis: returns the value of this
    getTypeName: returns the type of this as a string. This is the name of the function stored in the constructor field of this, if available, otherwise the object's [[Class]] internal property.
    getFunction: returns the current function
    getFunctionName: returns the name of the current function, typically its name property. If a name property is not available an attempt will be made to try to infer a name from the function's context.
    getMethodName: returns the name of the property of this or one of its prototypes that holds the current function
    getFileName: if this function was defined in a script returns the name of the script
    getLineNumber: if this function was defined in a script returns the current line number
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

}
