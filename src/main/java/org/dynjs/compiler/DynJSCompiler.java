/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.compiler;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DynJSCompiler {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String PACKAGE = "org.dynjs.gen.".replace('.', '/');
    private final DynJSConfig config;

    public DynJSCompiler(DynJSConfig config) {
        this.config = config;
    }

    public Function compile(final DynFunction arg) {
        final String className = PACKAGE + "AnonymousDynFunction" + counter.incrementAndGet();
        JiteClass jiteClass = new JiteClass(className, p(DynFunction.class), new String[]{p(Function.class)}) {{
            defineMethod("<init>", ACC_PUBLIC, sig(void.class),
                    newCodeBlock()
                            .aload(0)
                            .invokespecial(p(DynFunction.class), "<init>", sig(void.class))
                            .voidreturn()
            );
            defineMethod("call", ACC_PUBLIC, sig(Object.class, DynThreadContext.class, Object[].class), fillCallStack(alwaysReturnWrapper(arg.getCodeBlock())));

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
        Class<Function> functionClass = (Class<Function>) defineClass(jiteClass);
        try {
            return functionClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CodeBlock fillCallStack(CodeBlock codeBlock) {
        return newCodeBlock()
                .aload(Arities.CONTEXT)
                .aload(Arities.THIS)
                .aload(Arities.ARGS)
                .invokestatic(p(RT.class), "callHelper", sig(Function.class, DynThreadContext.class, DynFunction.class, Object[].class))
                .dup()
                .astore(0)
                .aload(Arities.CONTEXT)
                .invokevirtual(p(DynThreadContext.class), "getCallStack", sig(Deque.class))
                .swap()
                .invokeinterface(p(Deque.class), "push", sig(void.class, Object.class))
                .append(codeBlock)
                .dup()
                .aload(Arities.CONTEXT)
                .invokevirtual(p(DynThreadContext.class), "getCallStack", sig(Deque.class))
                .invokeinterface(p(Deque.class), "pop", sig(Object.class))
                .pop();
    }

    private CodeBlock alwaysReturnWrapper(CodeBlock codeBlock) {
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
                defineMethod("execute", ACC_PUBLIC | ACC_VARARGS, sig(void.class, DynThreadContext.class), getCodeBlock());
            }

            private CodeBlock getCodeBlock() {
                final CodeBlock block = newCodeBlock();
                for (Statement statement : statements) {
                    block.append(statement.getCodeBlock());
                }
                return block.voidreturn();
            }
        };
        Class<?> functionClass = defineClass(jiteClass);
        try {
            Constructor<?> ctor = functionClass.getDeclaredConstructor(Statement[].class);
            return (Script) ctor.newInstance(new Object[]{statements});
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Class<?> defineClass(JiteClass jiteClass) {
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);

        if (config.isDebug()) {
            ClassReader reader = new ClassReader(bytecode);
            CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
        }
        return new DynamicClassLoader(config.getClassLoader()).define(jiteClass.getClassName().replace('/', '.'), bytecode);
    }

    public static interface Types {

        String RUNTIME = p(DynJS.class);
        String CONTEXT = p(DynThreadContext.class);
        String Scope = p(Scope.class);
    }

    public static interface Arities {

        int THIS = 0;
        int CONTEXT = 1;
        int ARGS = 2;
    }

    public static interface Helper {

        CodeBlock EMPTY_CODEBLOCK = newCodeBlock();
    }
}
