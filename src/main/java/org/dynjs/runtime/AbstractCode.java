package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.VariableDeclarationStatement;
import org.dynjs.parser.statement.FunctionDeclaration;

public abstract class AbstractCode implements JSCode {
    
    private Statement[] statements;
    private boolean strict;

    public AbstractCode(Statement[] statements) {
        this( statements, false );
    }
    
    public AbstractCode(Statement[] statements, boolean strict) {
        this.statements = statements;
        this.strict = strict;
    }
    
    public boolean isStrict() {
        return this.strict;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        for ( int i = 0 ; i < statements.length ; ++i ) {
            if ( statements[i] instanceof FunctionDeclaration ) {
                FunctionDeclaration fn = (FunctionDeclaration) statements[i];
                if ( fn.getIdentifier() != null ) {
                    decls.add( fn );
                }
            }
        }
        return decls;
    }
    
    public List<VariableDeclarationStatement> getVariableDeclarations() {
        List<VariableDeclarationStatement> decls = new ArrayList<>();
        for ( int i = 0 ; i < statements.length ; ++i ) {
            if ( statements[i] instanceof VariableDeclarationStatement ) {
                VariableDeclarationStatement var = (VariableDeclarationStatement) statements[i];
                decls.add( var );
            }
        }
        return decls;
        
    }
    

}
