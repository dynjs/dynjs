package org.dynjs.runtime.linker.java;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.Types.Undefined;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.tree.LabelNode;

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
            if (param == int.class) {
                block.iload(i + 1);
                // fn context this args args index int
                block.invokestatic(p(Integer.class), "valueOf", sig(Integer.class, int.class));
                // fn context this args args index Integer
            } else if (param == boolean.class) {
                block.iload(i + 1);
                // fn context this args args index int
                block.invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                // fn context this args args index Boolean
            } else {
                block.aload(i + 1);
                // fn context this args args index arg-I
            }
            block.aastore();
            // fn context this args
        }

        Class<?> returnType = method.getReturnType();
        // JavaScript returns all numbers as Longs
        if (Number.class.isAssignableFrom(returnType) || returnType == int.class) {
            returnType = Long.class;
        }
        // fn context this args
        block.invokedynamic("dyn:call", sig(returnType, Object.class, ExecutionContext.class, Object.class, Object[].class),
                DynJSBootstrapper.HANDLE, DynJSBootstrapper.ARGS);
        // result
        if (returnType == Void.TYPE) {
            // aconst_null();
            handleDefaultReturnValue(block);
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
                if (param == int.class || param == boolean.class) {
                    block.iload(i + 1);
                } else {
                    block.aload(i + 1);
                }
            }
            block.invokespecial(p(superClass), method.getName(), sig(signature));
            if (method.getReturnType() == Void.TYPE) {
                // aconst_null();
                handleDefaultReturnValue(block);
            }
        } else {
            handleDefaultReturnValue(block);
        }
    }

    protected abstract void handleDefaultReturnValue(CodeBlock block);

}
