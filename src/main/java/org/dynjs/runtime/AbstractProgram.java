package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class AbstractProgram extends AbstractCode implements JSProgram {

    public AbstractProgram(Statement block) {
        super(block);
    }

    public AbstractProgram(Statement block, boolean strict) {
        super(block, strict);
    }
    
    public String toString() {
        return "[Program: fileName=" + getFileName() + "; strict=" + isStrict() + "]";
    }

}
