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
    
    public String toString() {
        return this.debugContext + " (" + this.fileName + ":" + this.lineNumber + ")";
    }

}
