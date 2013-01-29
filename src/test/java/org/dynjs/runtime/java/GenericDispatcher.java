package org.dynjs.runtime.java;

public class GenericDispatcher<T> {
    
    public Object handle(GenericHandler<T> handler) {
        return handler.handle( null );
    }

}
