package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;
import org.dynjs.parser.statement.VariableDeclarationStatement;

public abstract class AbstractCode implements JSCode {

    private Statement block;
    private boolean strict;

    public AbstractCode(Statement block) {
        this( block, false );
    }

    public AbstractCode(Statement block, boolean strict) {
        this.block = block;
        this.strict = strict;
    }

    public boolean isStrict() {
        return this.strict;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (block instanceof BlockStatement) {
            return ((BlockStatement) block).getFunctionDeclarations();
        }
        
        if ( block instanceof FunctionDeclaration ) {
            return Collections.singletonList( (FunctionDeclaration) block );
        }
        
        return Collections.emptyList();
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        if (block instanceof BlockStatement) {
            return ((BlockStatement) block).getVariableDeclarations();
        }
        
        if ( block instanceof VariableDeclarationStatement ) {
            return ((VariableDeclarationStatement)block).getVariableDeclarations();
        }
        
        return Collections.emptyList();
    }

}
