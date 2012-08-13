package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;

public abstract class AbstractCode implements JSCode {

    private BlockStatement block;
    private boolean strict;

    public AbstractCode(BlockStatement block) {
        this( block, false );
    }

    public AbstractCode(BlockStatement block, boolean strict) {
        this.block = block;
        this.strict = strict;
    }

    public boolean isStrict() {
        return this.strict;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return block.getFunctionDeclarations();
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return block.getVariableDeclarations();
    }

}
