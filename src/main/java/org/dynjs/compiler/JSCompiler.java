package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynJS;
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
    private DynJS runtime;

    public JSCompiler(DynJS runtime, Config config) {
        this.runtime = runtime;
        this.config = config;
        this.programCompiler = new ProgramCompiler(runtime, this.config);
        this.functionCompiler = new FunctionCompiler(runtime, this.config);
        this.basicBlockCompiler = new BasicBlockCompiler(runtime, this.config);
    }

    public JSProgram compileProgram(Statement statement, boolean forceStrict) {
        return this.programCompiler.compile(statement, forceStrict);
    }
    
    public JSProgram compileProgram(ExecutionContext context, Statement statement, boolean forceStrict) {
        return this.programCompiler.compile(context, statement, forceStrict);
    }

    public JSFunction compileFunction(ExecutionContext context, String[] formalParameters, Statement body, boolean containedInStrictCode) {
        return this.functionCompiler.compile(context, formalParameters, body, containedInStrictCode );
    }

    public BasicBlock compileBasicBlock(String grist, Statement body) {
        return this.basicBlockCompiler.compile(grist, body);
    }

}
