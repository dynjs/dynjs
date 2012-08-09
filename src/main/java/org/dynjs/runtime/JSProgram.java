package org.dynjs.runtime;

public interface JSProgram extends JSCode {
    void execute(ExecutionContext context);
}
