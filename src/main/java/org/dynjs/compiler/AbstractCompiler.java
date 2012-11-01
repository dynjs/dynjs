package org.dynjs.compiler;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.AbstractCodeGeneratingVisitor;
import org.dynjs.codegen.BasicBytecodeGeneratingVisitor;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

public class AbstractCompiler {

    private final AtomicInteger counter = new AtomicInteger();
    private Class<? extends AbstractCodeGeneratingVisitor> codeGenClass;
    private DynJS runtime;
    private Config config;
    private String type;

    public AbstractCompiler(Class<? extends AbstractCodeGeneratingVisitor> codeGenClass, DynJS runtime, Config config, String type) {
        this.codeGenClass = codeGenClass;
        this.runtime = runtime;
        this.config = config;
        this.type = type;
    }

    public Config getConfig() {
        return this.config;
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

    public int nextCounterValue() {
        return this.counter.getAndIncrement();
    }

    public String nextClassName() {
        return nextClassName("");
    }

    public String nextClassName(String grist) {
        return this.config.getBasePackage().replace('.', '/') + "/" + grist + type + nextCounterValue();
    }

    protected Class<?> defineClass(JiteClass jiteClass) {
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);

        if (config.isDebug()) {
            ClassReader reader = new ClassReader(bytecode);
            CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
        }
        return new DynamicClassLoader(config.getClassLoader()).define(jiteClass.getClassName().replace('/', '.'), bytecode);
    }

    protected AbstractCodeGeneratingVisitor newCodeGeneratingVisitor(ExecutionContext context) {
        try {
            Constructor<? extends AbstractCodeGeneratingVisitor> ctor = this.codeGenClass.getConstructor(BlockManager.class);
            AbstractCodeGeneratingVisitor instance = ctor.newInstance(context.getBlockManager());
            return instance;
        } catch (Exception e) {
            return new BasicBytecodeGeneratingVisitor(context.getBlockManager());
        }
    }

}
