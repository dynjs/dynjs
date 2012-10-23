package org.dynjs.compiler;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.StringLiteralExpression;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynamicClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

public class AbstractCompiler {

    private final AtomicInteger counter = new AtomicInteger();
    private DynJS runtime;
    private Config config;
    private String type;

    public AbstractCompiler(DynJS runtime, Config config, String type) {
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
    
}
