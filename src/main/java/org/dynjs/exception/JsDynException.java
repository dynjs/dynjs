package org.dynjs.exception;

public class JsDynException extends RuntimeException {
    public JsDynException(){
        super();
    }

    public JsDynException(Exception e) {
        super(e);
    }
}
