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
import org.dynjs.runtime.DynFunction;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.Script;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DynJSCompiler {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String PACKAGE = "org.dynjs.gen.".replace('.', '/');
    private final DynJSConfig config;

    public DynJSCompiler(DynJSConfig config) {
        this.config = config;
    }

    public Object compileExceptionHandler(DynThreadContext context, final CodeBlock codeBlock, final String identifier) {
        return internalCompile("ExceptionHandler", context, codeBlock, new String[]{identifier}, false);
    }

    public Object compileBasicBlock(DynThreadContext context, final CodeBlock codeBlock, final String[] arguments) {
        return internalCompile("BasicBlock", context, codeBlock, arguments, false);
    }

    public Object compileTryBlock(DynThreadContext context, CodeBlock codeBlock) {
        return internalCompile("TryBlock", context, codeBlock, new String[]{}, false);
    }

    public Object compileFinallyBlock(DynThreadContext context, CodeBlock codeBlock) {
        return internalCompile("FinallyBlock", context, codeBlock, new String[]{}, false);
    }

    public Object compile(DynThreadContext context, final CodeBlock codeBlock, final String[] arguments) {
        return internalCompile("AnonymousDynFunction", context, codeBlock, arguments, true);
    }

    private Object internalCompile(String prefix, DynThreadContext context, final CodeBlock codeBlock, final String[] arguments, boolean wrap) {
        final String className = generateNameFromBase(prefix);
        JiteClass jiteClass = new JiteClass(className, p(DynFunction.class), new String[]{p(Function.class)}) {{
            defineMethod("<init>", ACC_PUBLIC, sig(void.class),
                    new CodeBlock() {{
                        aload(0);
                        invokespecial(p(DynFunction.class), "<init>", sig(void.class));
                        voidreturn();
                    }});
            defineMethod("call", ACC_PUBLIC, Signatures.FCALL_WITH_SELF, new CodeBlock() {{
                append(alwaysReturnWrapper(codeBlock));
            }});

            defineMethod("getParameters", ACC_PUBLIC, sig(String[].class), new CodeBlock() {{
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
        context.getCapturedScopeStore().put(functionClass, context.getScope());
        try {
            Function function = functionClass.newInstance();
            if (wrap) {
                return wrapFunction(context, function);
            } else {
                return function;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNameFromBase(String prefix) {
        return PACKAGE + prefix + counter.incrementAndGet();
    }

    public static DynObject wrapFunction(final Object prototype, final Function function) {
        return new InternalDynObject(prototype, function);
    }

    public static DynObject wrapFunction(final DynThreadContext context, final Function function) {
        return wrapFunction(context.getBuiltin("Function"), function);
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
                        new CodeBlock() {{
                            aload(0);
                            aload(1);
                            invokespecial(p(BaseScript.class), "<init>", sig(void.class, Statement[].class));
                            voidreturn();
                        }});

                defineMethod("execute", ACC_PUBLIC | ACC_VARARGS, sig(void.class, Scope.class, DynThreadContext.class), getCodeBlock());
            }

            private CodeBlock getCodeBlock() {
                final CodeBlock block = new CodeBlock();
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
        int SELF = 1;
        int CONTEXT = 2;
        int ARGS = 3;
    }

    public static interface Signatures {

        String FCALL = sig(Object.class, DynThreadContext.class, Object[].class);
        String FCALL_WITH_SELF = sig(Object.class, Object.class, DynThreadContext.class, Object[].class);
        String ARITY_2 = sig(Object.class, Object.class, Object.class);
    }

    public static interface Helper {

        CodeBlock EMPTY_CODEBLOCK = new CodeBlock();
    }

    public static class InternalDynObject extends DynObject {

        public InternalDynObject(Object prototype, Object function) {
            setProperty("prototype", prototype);
            if (function != null) {
                setProperty("call", function);
                setProperty("construct", function);
            }
        }

        public Boolean hasInstance(Object object) {
            if (object instanceof DynObject) {
                Object proto = getProperty("prototype").getAttribute("value");
                return proto == object;
            }
            return false;
        }

    }
}
