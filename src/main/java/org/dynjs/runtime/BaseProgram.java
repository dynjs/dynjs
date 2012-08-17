package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class BaseProgram extends AbstractCode implements JSProgram {

    public BaseProgram(Statement block) {
        super(block);
    }

    public BaseProgram(Statement block, boolean strict) {
        super(block, strict);
    }

}
