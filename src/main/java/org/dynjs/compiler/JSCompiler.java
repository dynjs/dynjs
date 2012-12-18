package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.toplevel.BytecodeBasicBlockCompiler;
import org.dynjs.compiler.bytecode.toplevel.BytecodeFunctionCompiler;
import org.dynjs.compiler.bytecode.toplevel.BytecodeProgramCompiler;
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

        this.programCompiler = new BytecodeProgramCompiler(config, factory);
        this.functionCompiler = new BytecodeFunctionCompiler(config, factory);
        this.basicBlockCompiler = new BytecodeBasicBlockCompiler(config, factory);
    }

    public JSProgram compileProgram(ExecutionContext context, ProgramTree program, boolean forceStrict) {
        return this.programCompiler.compile(context, program, forceStrict);
    }

    public JSFunction compileFunction(ExecutionContext context, String[] formalParameters, BlockStatement body, boolean containedInStrictCode) {
        return this.functionCompiler.compile(context, formalParameters, body, containedInStrictCode);
    }

    public BasicBlock compileBasicBlock(ExecutionContext context, String grist, Statement body) {
        return this.basicBlockCompiler.compile(context, grist, body);
    }

}
