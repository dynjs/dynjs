package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;

public class BasicBlockDelegate implements BasicBlock {

    private BasicBlock delegate;

    public BasicBlockDelegate(BasicBlock initial) {
        this.delegate = initial;
    }
    
    public void setDelegate(BasicBlock delegate) {
        this.delegate = delegate;
    }
    
    public BasicBlock getDelegate() {
        return this.delegate;
    }
    
    @Override
    public Completion call(ExecutionContext context) {
        return this.delegate.call( context );
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.delegate.getVariableDeclarations();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.delegate.getFunctionDeclarations();
    }

    @Override
    public String getFileName() {
        return this.delegate.getFileName();
    }

    @Override
    public boolean isStrict() {
        return this.delegate.isStrict();
    }

}
