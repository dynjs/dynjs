package org.dynjs.compiler.jit;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.BasicBlockCompiler;
import org.dynjs.compiler.bytecode.BytecodeBasicBlockCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.CompilableBasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.interp.InterpretedBasicBlock;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;

public class JITBasicBlockCompiler implements BasicBlockCompiler {

    private InterpretingVisitorFactory interpFactory;
    private BytecodeBasicBlockCompiler jitCompiler;
    private Executor compilationQueue;

    public JITBasicBlockCompiler(Config config, InterpretingVisitorFactory interpFactory, CodeGeneratingVisitorFactory factory) {
        this.interpFactory = interpFactory;
        this.jitCompiler = new BytecodeBasicBlockCompiler(config, factory);
        this.compilationQueue = Executors.newFixedThreadPool(5);
    }

    @Override
    public BasicBlock compile(ExecutionContext context, String grist, Statement body, boolean strict) {
        int statementNumber = body.getStatementNumber();
        Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if ( code != null ) {
            return code;
        }
        InterpretedBasicBlock initial = new InterpretedBasicBlock(this.interpFactory, body, strict);
        code = new CompilableBasicBlock(this, grist, initial);
        entry.setCompiled(code);
        return code;
    }

    private BasicBlock jitCompile(ExecutionContext context, String grist, Statement body, boolean strict) {
        return this.jitCompiler.compile(context, grist, body, strict);
    }

    public void requestJitCompilation(ExecutionContext context, CompilableBasicBlock block) {
        this.compilationQueue.execute(new CompileRequest(context, block));
    }

    private class CompileRequest implements Runnable {

        private ExecutionContext context;
        private CompilableBasicBlock block;

        public CompileRequest(ExecutionContext context, CompilableBasicBlock block) {
            this.context = context;
            this.block = block;
        }

        @Override
        public void run() {
            BasicBlock delegate = this.block.getDelegate();
            if (delegate instanceof InterpretedBasicBlock) {
                BasicBlock compiled = jitCompile(this.context, block.getGrist(), ((InterpretedBasicBlock)delegate).getBody(), block.isStrict());
                this.block.setDelegate( compiled );
            }
        }
    }

}
