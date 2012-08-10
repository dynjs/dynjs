package org.dynjs.runtime;

public interface BasicBlock {
    
    Completion invoke(ExecutionContext context);

}
