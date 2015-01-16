package org.dynjs.runtime;

import org.dynjs.exception.DynJSException;

public class ExceededResourcesException extends DynJSException {
    public ExceededResourcesException(String message) {
        super(message);
    }
}
