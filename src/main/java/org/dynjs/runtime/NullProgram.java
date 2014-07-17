package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;


public class NullProgram implements JSProgram {

    private String filename;

    public NullProgram(String filename) {
        this.filename = filename;
    }

    @Override
    public Completion execute(ExecutionContext context) {
        return Completion.createNormal();
    }

    @Override
    public BlockManager getBlockManager() {
        return null;
    }

    @Override
    public String getFileName() {
        return this.filename;
    }

    @Override
    public boolean isStrict() {
        return false;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.emptyList();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return Collections.emptyList();
    }
    

}
