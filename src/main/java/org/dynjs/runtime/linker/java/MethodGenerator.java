package org.dynjs.runtime.linker.java;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.Types.Undefined;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static me.qmx.jitescript.util.CodegenUtils.*;

public abstract class MethodGenerator {

    public abstract void defineMethod(final Method method, final JiteClass jiteClass, final Class<?> superClass);

    protected void callJavascriptImplementation(Method method, JiteClass jiteClass, CodeBlock block, LabelNode noImpl) {
        final Class<?>[] params = method.getParameterTypes();
        
        LabelNode internalNoImpl = new LabelNode();
        LabelNode internalHasImpl = new LabelNode();

        block.aload(Arities.THIS);
        // this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "implementation", ci(JSObject.class));
        // obj

        block.aload(Arities.THIS);
        // obj this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "context", ci(ExecutionContext.class));
        // obj context

        block.ldc(method.getName());
        // obj context name

        block.invokeinterface(p(JSObject.class), "get", sig(Object.class, ExecutionContext.class, String.class));
        // fn
        block.dup();
        // fn fn
        block.getstatic(p(Types.class), "UNDEFINED", ci(Undefined.class));
        // fn fn undef

        block.if_acmpeq(internalNoImpl);
        // fn
        
        block.go_to( internalHasImpl );
        
        block.label( internalNoImpl );
        // fn
        block.pop();
        // empty
        block.go_to( noImpl );
        // EXIT
        
        block.label( internalHasImpl );
        // fn
        block.aload(Arities.THIS);
        // fn this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "context", ci(ExecutionContext.class));
        // fn context

        block.aload(Arities.THIS);
        // fn context this

        block.ldc(params.length);
        // fn context this length
        block.anewarray(p(Object.class));
        // fn context this args

        for (int i = 0; i < params.length; ++i) {
            Class<?> param = params[i];
            block.dup();
            // fn context this args args
            block.ldc(i);
            // fn context this args args index
            if (param == int.class || param == short.class || param == byte.class) {
                block.iload(i + 1);
                // fn context this args args index int
                block.invokestatic(p(Integer.class), "valueOf", sig(Integer.class, int.class));
                // fn context this args args index Integer
            } else if (param == long.class) {
                block.lload(i + 1);
                block.invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
            } else if (param == float.class) {
                block.fload(i + 1);
                block.invokestatic(p(Float.class), "valueOf", sig(Float.class, float.class));
            } else if (param == double.class) {
                block.dload(i + 1);
                block.invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
            } else if (param == boolean.class) {
                block.iload(i + 1);
                // fn context this args args index int
                block.invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                // fn context this args args index Boolean
            } else if (param == char.class) {
                block.iload(i + 1);
                // fn context this args args index char
                block.invokestatic(p(String.class), "valueOf", sig(String.class, char.class));
                // fn context this args args index String
            } else {
                block.aload(i + 1);
                // fn context this args args index arg-I
            }
            block.aastore();
            // fn context this args
        }

        Class<?> returnType = method.getReturnType();
        // Sort out our real return type later and all numerics as Numbers
        if (Number.class.isAssignableFrom(returnType) || returnType == int.class ||
                returnType == short.class || returnType == long.class ||
                returnType == float.class || returnType == double.class ||
                returnType == byte.class) {
            returnType = Number.class;
        } else if (returnType == char.class || returnType == Character.class) {
            returnType = String.class;
        }
        // fn context this args
        block.invokedynamic("dyn:call", sig(returnType, Object.class, ExecutionContext.class, Object.class, Object[].class),
                DynJSBootstrapper.HANDLE, DynJSBootstrapper.ARGS);
        // result
        if (returnType == Void.TYPE) {
            // aconst_null();
            handleDefaultReturnValue(returnType, block);
        }
    }

    protected void callSuperImplementation(Method method, Class<?> superClass, CodeBlock block) {
        boolean superMethodFound = false;
        try {
            superMethodFound = (superClass.getMethod(method.getName(), method.getParameterTypes()) != null);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }


        final boolean hasSuper = superMethodFound || Modifier.isProtected(method.getModifiers());
        if (hasSuper) {
            final Class<?>[] params = method.getParameterTypes();
            final Class<?>[] signature = new Class<?>[params.length + 1];

            for (int i = 1; i < params.length + 1; ++i) {
                signature[i] = params[i - 1];
            }

            signature[0] = method.getReturnType();


            block.aload(Arities.THIS);
            for (int i = 0; i < params.length; ++i) {
                Class<?> param = params[i];
                if (param == int.class || param == short.class ||
                        param == boolean.class || param == char.class ||
                        param == byte.class) {
                    block.iload(i + 1);
                } else if (param == long.class) {
                    block.lload(i + 1);
                } else if (param == float.class) {
                    block.fload(i + 1);
                } else if (param == double.class) {
                    block.dload(i + 1);
                } else {
                    block.aload(i + 1);
                }
            }
            block.invokespecial(p(superClass), method.getName(), sig(signature));
            if (method.getReturnType() == Void.TYPE) {
                handleDefaultReturnValue(method.getReturnType(), block);
            }
        } else {
            handleDefaultReturnValue(method.getReturnType(), block);
        }
    }

    protected abstract void handleDefaultReturnValue(Class<?> returnType, CodeBlock block);

}
