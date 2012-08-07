package org.dynjs.runtime;

public interface JSCallable {
    
    Object call(JSObject self, String...args);

}
