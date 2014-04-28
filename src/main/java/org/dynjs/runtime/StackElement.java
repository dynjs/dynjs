package org.dynjs.runtime;

public class StackElement {

    public String fileName;
    public int lineNumber;
    public String debugContext;

    public StackElement(String fileName, int lineNumber, String debugContext) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.debugContext = debugContext;
    }

    public StackTraceElement toStackTraceElement() {
        String cn = "<global>";
        String fn = null;
        int dotLoc = this.debugContext.indexOf(".");
        if (dotLoc > 0) {
            cn = this.debugContext.substring(0, dotLoc);
            fn = this.debugContext.substring(dotLoc + 1);
        } else {
            fn = this.debugContext;
        }
        return new StackTraceElement(cn, fn, this.fileName, this.lineNumber);
    }

    public String toString() {
        return this.debugContext + " (" + this.fileName + ":" + this.lineNumber + ")";
    }

}
