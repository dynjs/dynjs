package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.DeclareVarStatement;
import org.dynjs.parser.statement.FunctionStatement;

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
    public List<FunctionStatement> getFunctionDeclarations() {
        List<FunctionStatement> decls = new ArrayList<>();
        for ( int i = 0 ; i < statements.length ; ++i ) {
            if ( statements[i] instanceof FunctionStatement ) {
                FunctionStatement fn = (FunctionStatement) statements[i];
                if ( fn.getIdentifier() != null ) {
                    decls.add( fn );
                }
            }
        }
        return decls;
    }
    
    public List<DeclareVarStatement> getVariableDeclarations() {
        List<DeclareVarStatement> decls = new ArrayList<>();
        for ( int i = 0 ; i < statements.length ; ++i ) {
            if ( statements[i] instanceof DeclareVarStatement ) {
                DeclareVarStatement var = (DeclareVarStatement) statements[i];
                decls.add( var );
            }
        }
        return decls;
        
    }
    

}
