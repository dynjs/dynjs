package org.dynjs.compiler.bytecode.toplevel;

import java.util.concurrent.atomic.AtomicInteger;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.AbstractBytecodeCompiler;

public class AbstractTopLevelCompiler extends AbstractBytecodeCompiler {

    private String type;
    private AtomicInteger counter = new AtomicInteger();

    public AbstractTopLevelCompiler(Config config, CodeGeneratingVisitorFactory factory, String type) {
        super(config, factory);
        this.type = type;
    }

    public String nextClassName(String grist) {
        return getConfig().getBasePackage().replace('.', '/') + "/" + grist + type + nextCounterValue();
    }

    public String nextClassName() {
        return nextClassName("");
    }
    
    private int nextCounterValue() {
        return this.counter.getAndIncrement();
    }

}
