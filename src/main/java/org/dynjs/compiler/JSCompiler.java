package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.ByteCodeFunctionCompiler;
import org.dynjs.compiler.bytecode.BytecodeBasicBlockCompiler;
import org.dynjs.compiler.bytecode.BytecodeProgramCompiler;
import org.dynjs.compiler.interpreter.InterpretingBasicBlockCompiler;
import org.dynjs.compiler.interpreter.InterpretingFunctionCompiler;
import org.dynjs.compiler.interpreter.InterpretingProgramCompiler;
import org.dynjs.compiler.jit.JITBasicBlockCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;

public class JSCompiler {

    private ProgramCompiler programCompiler;
    private FunctionCompiler functionCompiler;
    private BasicBlockCompiler basicBlockCompiler;

    public JSCompiler(Config config) {
        CodeGeneratingVisitorFactory factory = new CodeGeneratingVisitorFactory(config.isInvokeDynamicEnabled());
        InterpretingVisitorFactory interpFactory = new InterpretingVisitorFactory( config.isInvokeDynamicEnabled() );

        switch ( config.getCompileMode() ) {
        case OFF:
            this.basicBlockCompiler = new InterpretingBasicBlockCompiler( interpFactory );
            this.functionCompiler = new InterpretingFunctionCompiler( interpFactory );
            this.programCompiler = new InterpretingProgramCompiler( interpFactory );
            break;
        case FORCE:
            this.basicBlockCompiler = new BytecodeBasicBlockCompiler(config, factory);
            this.functionCompiler = new ByteCodeFunctionCompiler();
            this.programCompiler = new BytecodeProgramCompiler();
            break;
        case JIT: // FIXME: this should go away when IR becomes default (config.isJitEnabled())
            this.basicBlockCompiler = new JITBasicBlockCompiler(config, interpFactory, factory);
            this.functionCompiler = new ByteCodeFunctionCompiler(); // FIXME: Add JIT
            this.programCompiler = new BytecodeProgramCompiler(); // FIXME: Add JIT
            break;
        }
    }

    public JSProgram compileProgram(CompilationContext context, ProgramTree program, boolean forceStrict) {
        return this.programCompiler.compile(context, program, forceStrict);
    }

    public JSFunction compileFunction(CompilationContext context, String identifier, String[] formalParameters, Statement body, boolean containedInStrictCode) {
        return this.functionCompiler.compile(context, identifier, formalParameters, body, containedInStrictCode);
    }

    public BasicBlock compileBasicBlock(CompilationContext context, String grist, Statement body, boolean strict) {
        return this.basicBlockCompiler.compile(context, grist, body, strict);
    }

}
