package org.dynjs.runtime.interp;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

import java.util.List;

public class InterpretedBasicBlock implements BasicBlock {

    private InterpretingVisitorFactory factory;
    private Statement body;
    private boolean strict;

    public InterpretedBasicBlock(InterpretingVisitorFactory factory, Statement body, boolean strict) {
        this.factory = factory;
        this.body = body;
        this.strict = strict;
    }

    @Override
    public Completion call(ExecutionContext context) {
        //CodeVisitor visitor = factory.createVisitor( context.getBlockManager() );
        //return (Completion) this.body.accept(context, visitor, this.strict);
        return this.body.interpret(context);
    }
    
    public Statement getBody() {
        return this.body;
    }

    @Override
    public String getFileName() {
        Position position = this.body.getPosition();
        if ( position != null )  {
            return position.getFileName();
        }
        
        return "<eval>";
    }

    @Override
    public boolean isStrict() {
        return this.strict;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.body.getVariableDeclarations();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.body.getFunctionDeclarations();
    }

}
