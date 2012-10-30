package org.dynjs.exception;

public class ClassFileOutputException extends DynJSException {

    public ClassFileOutputException(String className, Throwable cause) {
        super(cause);
        this.className = className;
    }

    public String getMessage() {
        return "Unable to write class '" + this.className + "': " + super.getMessage();
    }

    public String getClassName() {
        return this.className;
    }

    private String className;
}
