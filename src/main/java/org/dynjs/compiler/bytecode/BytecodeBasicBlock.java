package org.dynjs.compiler.bytecode;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BasicBlock;

public abstract class BytecodeBasicBlock implements BasicBlock {

    private String fileName;
    private boolean strict;
    private List<VariableDeclaration> variableDeclarations;
    private List<FunctionDeclaration> functionDeclrataions;

    public BytecodeBasicBlock(String fileName, boolean strict, List<VariableDeclaration> variableDeclarations, List<FunctionDeclaration> functionDeclarations) {
        this.fileName = fileName;
        this.strict= strict;
        this.variableDeclarations = variableDeclarations;
        this.functionDeclrataions = functionDeclarations;
    }
    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public boolean isStrict() {
        return this.strict;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.variableDeclarations;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.functionDeclrataions;
    }

}
