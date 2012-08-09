package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class BaseProgram extends AbstractCode implements JSProgram {

    public BaseProgram(Statement[] statements) {
        super(statements);
    }
    
    public BaseProgram(Statement[] statements, boolean strict) {
        super( statements, strict );
    }

}
