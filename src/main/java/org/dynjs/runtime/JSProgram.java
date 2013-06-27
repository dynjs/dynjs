package org.dynjs.runtime;

public interface JSProgram extends JSCode {
    
    DynamicClassLoader getClassLoader();
    
    Completion execute(ExecutionContext context);

    String getFileName();
}
