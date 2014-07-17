package org.dynjs.runtime.wrapper;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.*;

public class JavascriptProgram implements JSProgram {

    private BlockManager blockManager;
    private BasicBlock code;

    public JavascriptProgram(BlockManager blockManager, BasicBlock code) {
        this.blockManager = blockManager;
        this.code = code;
    }

    @Override
    public boolean isStrict() {
        return this.code.isStrict();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.code.getFunctionDeclarations();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.code.getVariableDeclarations();
    }

    @Override
    public Completion execute(ExecutionContext context) {
        return this.code.call(context);
    }

    @Override
    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    @Override
    public String getFileName() {
        return this.code.getFileName();
    }
    
}
