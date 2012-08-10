package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSProgram;

public class JSCompiler {
    
    public static interface Arities {
        int THIS = 0;
        int SELF = 1;
        int EXECUTION_CONTEXT = 2;
        int ARGS = 3;
    }
    
    private Config config;
    private ProgramCompiler programCompiler;
    private FunctionCompiler functionCompiler;
    private BasicBlockCompiler basicBlockCompiler;
    
    public JSCompiler(Config config) {
        this.config = config;
        this.programCompiler = new ProgramCompiler( this.config );
        this.functionCompiler = new FunctionCompiler( this.config );
        this.basicBlockCompiler = new BasicBlockCompiler( this.config );
    }
    
    public JSProgram compileProgram(Statement...statements) {
        return this.programCompiler.compile( statements );
    }
    
    public JSFunction compileFunction(ExecutionContext context, boolean strict, String[] formalParameters, BlockStatement body) {
        return this.functionCompiler.compile( context, strict, formalParameters, body );
    }
    
    public BasicBlock compileBasicBlock(String grist, BlockStatement body) {
        return this.basicBlockCompiler.compile( grist, body );
    }
    
}
