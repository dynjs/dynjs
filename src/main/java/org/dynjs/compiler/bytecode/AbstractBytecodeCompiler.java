package org.dynjs.compiler.bytecode;

import java.io.PrintWriter;

import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitor;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.DynamicClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

public abstract class AbstractBytecodeCompiler {
    
    private Config config;
    private CodeGeneratingVisitorFactory factory;

    public AbstractBytecodeCompiler(Config config, CodeGeneratingVisitorFactory factory) {
        this.config = config;
        this.factory = factory;
    }
    
    public AbstractBytecodeCompiler(AbstractBytecodeCompiler parent) {
        this.config = parent.config;
        this.factory = parent.factory;
    }
    
    public Config getConfig() {
        return this.config;
    }
    
    public CodeGeneratingVisitorFactory getFactory() {
        return this.factory;
    }
    
    public CodeGeneratingVisitor createVisitor(BlockManager blockManager) {
        return this.factory.create(blockManager);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T defineClass(DynamicClassLoader classLoader, JiteClass jiteClass) {
        //System.err.println( "defineClass: " + jiteClass.getClassName() );
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);

        if (config.isDebug()) {
            ClassReader reader = new ClassReader(bytecode);
            CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
        }
        return (T) classLoader.define(jiteClass.getClassName().replace('/', '.'), bytecode);
    }

}
