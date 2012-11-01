package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.codegen.BasicBytecodeGeneratingVisitor;
import org.dynjs.codegen.InvokeDynamicBytecodeGeneratingVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.Program;
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
        Class<? extends AbstractCodeGeneratingVisitor> codeGenClass = null;
        if ( config.isInvokeDynamicEnabled() ) {
            codeGenClass = InvokeDynamicBytecodeGeneratingVisitor.class;
        } else {
            codeGenClass = BasicBytecodeGeneratingVisitor.class;
        }
        this.programCompiler = new ProgramCompiler(codeGenClass, runtime, this.config);
        this.functionCompiler = new FunctionCompiler(codeGenClass, runtime, this.config);
        this.basicBlockCompiler = new BasicBlockCompiler(codeGenClass, runtime, this.config);
    }

    public JSProgram compileProgram(ExecutionContext context, Program program, boolean forceStrict) {
        return this.programCompiler.compile(context, program, forceStrict);
    }

    public JSFunction compileFunction(ExecutionContext context, String[] formalParameters, Statement body, boolean containedInStrictCode) {
        return this.functionCompiler.compile(context, formalParameters, body, containedInStrictCode );
    }

    public BasicBlock compileBasicBlock(ExecutionContext context, String grist, Statement body) {
        return this.basicBlockCompiler.compile(context, grist, body);
    }

}
