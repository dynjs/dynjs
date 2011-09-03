package org.dynjs.exception;

public class DynJSException extends RuntimeException {
    public DynJSException(){
        super();
    }

    public DynJSException(Exception e) {
        super(e);
    }
}
