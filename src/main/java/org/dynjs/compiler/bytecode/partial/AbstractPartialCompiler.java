package org.dynjs.compiler.bytecode.partial;

import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.AbstractBytecodeCompiler;
import org.dynjs.runtime.DynamicClassLoader;

public abstract class AbstractPartialCompiler extends AbstractBytecodeCompiler implements PartialCompiler {
    
    private DynamicClassLoader classLoader;

    public AbstractPartialCompiler(Config config, DynamicClassLoader classLoader, CodeGeneratingVisitorFactory factory) {
        super( config, factory );
        this.classLoader = classLoader;
    }
    
    public AbstractPartialCompiler(AbstractPartialCompiler parent) {
        super( parent );
        this.classLoader = parent.classLoader;
    }
    
    public DynamicClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    
    protected <T> Class<T> defineClass(JiteClass cls) {
        return defineClass( this.classLoader, cls );
    }
    

}
