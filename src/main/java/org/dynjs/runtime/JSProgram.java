package org.dynjs.runtime;

public interface JSProgram extends JSCode {
    Completion execute(ExecutionContext context);
    BlockManager getBlockManager();
    String getFileName();
}
