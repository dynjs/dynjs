package org.dynjs.runtime.java;

public class Dispatcher {
    
    public Object handle(GenericHandler<Thing> handler) {
        Thing thing = new Thing();
        
        return handler.handle( thing);
    }

}
