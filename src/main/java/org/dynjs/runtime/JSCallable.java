package org.dynjs.runtime;

public interface JSCallable {
    
    Object call(ExecutionContext context, Object self, Object...args);

}
