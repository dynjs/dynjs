package org.dynjs.compiler;

import me.qmx.internal.org.objectweb.asm.ClassReader;
import me.qmx.internal.org.objectweb.asm.util.CheckClassAdapter;
import me.qmx.internal.org.objectweb.asm.util.TraceClassVisitor;
import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynFunction;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.FunctionFactory;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.Script;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DynJSCompiler {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String PACKAGE = "org.dynjs.gen.".replace('.', '/');
    private static final boolean DEBUG = true;
    private final DynamicClassLoader classLoader = new DynamicClassLoader();

    public Function compile(final DynFunction arg) {
        final String className = PACKAGE + "AnonymousDynFunction" + counter.incrementAndGet();
        JiteClass jiteClass = new JiteClass(className, p(DynFunction.class), new String[]{p(Function.class)}) {{
            defineMethod("<init>", ACC_PUBLIC, sig(void.class),
                    newCodeBlock()
                            .aload(0)
                            .invokespecial(p(DynFunction.class), "<init>", sig(void.class))
                            .voidreturn()
            );
            defineMethod("call", ACC_PUBLIC, sig(DynAtom.class, DynThreadContext.class, Scope.class, DynAtom[].class), alwaysReturnWrapper(arg));

            defineMethod("getArguments", ACC_PUBLIC, sig(String[].class), new CodeBlock() {{
                String[] arguments = arg.getArguments();
                bipush(arguments.length);
                anewarray(p(String.class));
                for (int i = 0; i < arguments.length; i++) {
                    String argument = arguments[i];
                    dup();
                    bipush(i);
                    ldc(argument);
                    aastore();
                }
                areturn();
            }});
        }};
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);
        Class<Function> functionClass = (Class<Function>) defineClass(className, bytecode);
        return FunctionFactory.create(functionClass);
    }

    private CodeBlock alwaysReturnWrapper(DynFunction arg) {
        CodeBlock codeBlock = arg.getCodeBlock();
        CodeBlock paramPopulator = newCodeBlock()
                .aload(0)
                .aload(3)
                .invokedynamic("dynjs:compile:params", sig(DynFunction.class, DynFunction.class, DynAtom[].class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .astore(2);
        codeBlock = codeBlock.prepend(paramPopulator);

        if (!codeBlock.returns()) {
            codeBlock = codeBlock.aconst_null().areturn();
        }
        return codeBlock;
    }

    public Script compile(final Statement... statements) {
        String className = PACKAGE + "AnonymousDynScript" + counter.incrementAndGet();
        JiteClass jiteClass = new JiteClass(className, p(BaseScript.class), new String[]{p(Script.class)}) {
            {
                defineMethod("<init>", ACC_PUBLIC | ACC_VARARGS, sig(void.class, Statement[].class),
                        newCodeBlock()
                                .aload(0)
                                .aload(1)
                                .invokespecial(p(BaseScript.class), "<init>", sig(void.class, Statement[].class))
                                .voidreturn()
                );
                defineMethod("execute", ACC_PUBLIC | ACC_VARARGS, sig(void.class, DynThreadContext.class, Scope.class), getCodeBlock());
            }

            private CodeBlock getCodeBlock() {
                final CodeBlock block = newCodeBlock();
                for (Statement statement : statements) {
                    block.append(statement.getCodeBlock());
                }
                return block.voidreturn();
            }
        };
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);
        Class<?> functionClass = defineClass(className, bytecode);
        try {
            Constructor<?> ctor = functionClass.getDeclaredConstructor(Statement[].class);
            return (Script) ctor.newInstance(new Object[]{statements});
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Class<?> defineClass(String className, byte[] bytecode) {
        if (DEBUG) {
            ClassReader reader = new ClassReader(bytecode);
            CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
        }
        return classLoader.define(className.replace('/', '.'), bytecode);
    }

}
