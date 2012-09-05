package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSProgram;

public class JSCompiler {

    public static interface Arities {
        int THIS = 0;
        int EXECUTION_CONTEXT = 1;
        int COMPLETION = 2;
    }

    private Config config;
    private ProgramCompiler programCompiler;
    private FunctionCompiler functionCompiler;
    private BasicBlockCompiler basicBlockCompiler;

    public JSCompiler(Config config) {
        this.config = config;
        this.programCompiler = new ProgramCompiler(this.config);
        this.functionCompiler = new FunctionCompiler(this.config);
        this.basicBlockCompiler = new BasicBlockCompiler(this.config);
    }

    public JSProgram compileProgram(Statement statement, boolean forceStrict) {
        return this.programCompiler.compile(statement, forceStrict);
    }

    public JSFunction compileFunction(ExecutionContext context, String[] formalParameters, Statement body) {
        return this.functionCompiler.compile(context, formalParameters, body);
    }

    public BasicBlock compileBasicBlock(String grist, Statement body) {
        return this.basicBlockCompiler.compile(grist, body);
    }

}
