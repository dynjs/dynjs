package org.dynjs.exception;

public class InvalidModuleException extends DynJSException {

    private static final long serialVersionUID = 1L;

    public InvalidModuleException(Object module) {
        this.module = module;
    }

    public InvalidModuleException(Object module, String message) {
        super( message );
        this.module = module;
    }

    public Object getModule() {
        return this.module;
    }

    public String getMessage() {
        return "Invalid module '" + this.module.getClass().getName() + "': " + super.getMessage();
    }

    private Object module;

}
