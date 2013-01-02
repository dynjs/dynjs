package org.dynjs.runtime;

import java.util.concurrent.atomic.AtomicInteger;

import org.dynjs.compiler.jit.JITBasicBlockCompiler;

public class CompilableBasicBlock extends BasicBlockDelegate {
    
    private AtomicInteger counter = new AtomicInteger();
    private String grist;
    private JITBasicBlockCompiler compiler;

    public CompilableBasicBlock(JITBasicBlockCompiler compiler, String grist, BasicBlock initial) {
        super(initial);
        this.grist = grist;
        this.compiler = compiler;
    }
    
    @Override
    public Completion call(ExecutionContext context) {
        if ( counter.incrementAndGet() == 5 ) {
            enqueueCompilationRequest(context);
        }
        return super.call(context);
    }
    
    public String getGrist() {
        return this.grist;
    }
    
    protected void enqueueCompilationRequest(ExecutionContext context) {
        this.compiler.requestJitCompilation( context, this );
    }
    
    

}
