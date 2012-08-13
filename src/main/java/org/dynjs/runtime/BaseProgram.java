package org.dynjs.runtime;

import org.dynjs.parser.statement.BlockStatement;

public abstract class BaseProgram extends AbstractCode implements JSProgram {

    public BaseProgram(BlockStatement block) {
        super( block );
    }

    public BaseProgram(BlockStatement block, boolean strict) {
        super( block, strict );
    }

}
