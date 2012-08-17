package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class AbstractBasicBlock implements BasicBlock {

    private Statement body;

    public AbstractBasicBlock(Statement body) {
        this.body = body;
    }

}
