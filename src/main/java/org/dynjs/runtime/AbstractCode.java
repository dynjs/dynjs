package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.VariableDeclarationStatement;

public abstract class AbstractCode implements JSCode {

    private Statement block;
    private boolean strict;

    public AbstractCode(Statement block) {
        this(block, false);
    }

    public AbstractCode(Statement block, boolean strict) {
        this.block = block;
        this.strict = strict;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (this.block instanceof BlockStatement) {
            return ((BlockStatement) this.block).getFunctionDeclarations();
        }

        if (this.block instanceof FunctionDeclaration) {
            return Collections.singletonList((FunctionDeclaration) this.block);
        }

        return Collections.emptyList();
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        if (block instanceof BlockStatement) {
            return ((BlockStatement) block).getVariableDeclarations();
        }

        if (block instanceof VariableDeclarationStatement) {
            return ((VariableDeclarationStatement) block).getVariableDeclarations();
        }

        return Collections.emptyList();
    }

    public String getFileName() {
        String name = null;
        if (this.block.getPosition() != null) {
            name = this.block.getPosition().getFileName();
        }
        if (name == null) {
            name = "<eval>";
        }
        return name;
    }

}
