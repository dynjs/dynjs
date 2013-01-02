package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.BytecodeBasicBlockCompiler;
import org.dynjs.compiler.interpreter.InterpretingBasicBlockCompiler;
import org.dynjs.compiler.jit.JITBasicBlockCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSProgram;

public class JSCompiler {

    private Config config;
    private ProgramCompiler programCompiler;
    private FunctionCompiler functionCompiler;
    private BasicBlockCompiler basicBlockCompiler;
    private DynJS runtime;

    public JSCompiler(DynJS runtime, Config config) {
        this.runtime = runtime;
        this.config = config;

        CodeGeneratingVisitorFactory factory = new CodeGeneratingVisitorFactory(config.isInvokeDynamicEnabled());
        
        this.programCompiler = new ProgramCompiler();
        this.functionCompiler = new FunctionCompiler();

        switch ( config.getCompileMode() ) {
        case OFF:
            this.basicBlockCompiler = new InterpretingBasicBlockCompiler();
            break;
        case FORCE:
            this.basicBlockCompiler = new BytecodeBasicBlockCompiler(config, factory);
            break;
        case JIT:
            this.basicBlockCompiler = new JITBasicBlockCompiler(config, factory);
            break;
        }
    }

    public JSProgram compileProgram(ExecutionContext context, ProgramTree program, boolean forceStrict) {
        return this.programCompiler.compile(context, program, forceStrict);
    }

    public JSFunction compileFunction(ExecutionContext context, String identifier, String[] formalParameters, Statement body, boolean containedInStrictCode) {
        return this.functionCompiler.compile(context, identifier, formalParameters, body, containedInStrictCode);
    }

    public BasicBlock compileBasicBlock(ExecutionContext context, String grist, Statement body, boolean strict) {
        return this.basicBlockCompiler.compile(context, grist, body, strict);
    }

}
