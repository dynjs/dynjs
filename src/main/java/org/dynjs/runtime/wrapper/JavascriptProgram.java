package org.dynjs.runtime.wrapper;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public class JavascriptProgram implements JSProgram {
    
    private DynamicClassLoader classLoader;
    private BasicBlock code;

    public JavascriptProgram(DynamicClassLoader classLoader, BasicBlock code) {
        this.classLoader = classLoader;
        this.code = code;
    }
    
    public DynamicClassLoader getClassLoader() {
        return this.classLoader;
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
    public String getFileName() {
        return this.code.getFileName();
    }
    
}
