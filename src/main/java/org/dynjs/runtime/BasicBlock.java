package org.dynjs.runtime;

public interface BasicBlock {
    
    Object invoke(ExecutionContext context, Object self, Object[] args);

}
