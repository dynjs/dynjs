package org.dynjs.runtime;

public interface BasicBlock {

    Completion call(ExecutionContext context);

}
