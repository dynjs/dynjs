package org.dynjs.runtime.linker.java;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Method;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.Types.Undefined;
import org.objectweb.asm.tree.LabelNode;

public abstract class MethodGenerator {

    public abstract void defineMethod(final Method method, final JiteClass jiteClass, final Class<?> superClass);

    protected void callJavascriptImplementation(Method method, JiteClass jiteClass, CodeBlock block, LabelNode noImpl) {
        final Class<?>[] params = method.getParameterTypes();
        
        LabelNode internalNoImpl = new LabelNode();
        LabelNode internalHasImpl = new LabelNode();

        block.aload(Arities.THIS);
        block.getfield(jiteClass.getClassName().replace('.', '/'), "implementation", ci(JSObject.class));
        // obj

        block.aload(Arities.THIS);
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

        block.swap();
        // context fn

        block.aload(Arities.THIS);
        // context fn this

        block.ldc(params.length);
        block.anewarray(p(Object.class));
        // context fn this args

        for (int i = 0; i < params.length; ++i) {
            block.dup();
            // context fn this args args
            block.ldc(i);
            block.aload(i + 1);
            // context fn this args args arg-I
            block.aastore();
            // context fn this args
        }

        // context fn this args

        block.invokevirtual(p(ExecutionContext.class), "call", sig(Object.class, JSFunction.class, Object.class, Object[].class));
        // result
    }

    protected void callSuperImplementation(Method method, Class<?> superClass, CodeBlock block) {
        boolean superMethodFound = false;
        try {
            superMethodFound = (superClass.getMethod(method.getName(), method.getParameterTypes()) != null);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
        

        final boolean hasSuper = superMethodFound;
        if (hasSuper) {
            final Class<?>[] params = method.getParameterTypes();
            final Class<?>[] signature = new Class<?>[params.length + 1];

            for (int i = 1; i < params.length + 1; ++i) {
                signature[i] = params[i - 1];
            }

            signature[0] = method.getReturnType();
            

            block.aload(Arities.THIS);
            for (int i = 0; i < params.length; ++i) {
                block.aload(i + 1);
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

    protected void coerceForReturn(Method method, JiteClass jiteClass, CodeBlock block) {
        coerceTo(method.getReturnType(), jiteClass, block);
    }

    protected void coerceTo(Class<?> type, JiteClass jiteClass, CodeBlock block) {
        block.aload(Arities.THIS);
        block.invokevirtual(jiteClass.getClassName().replace('.', '/'), "getClass", sig(Class.class));
        block.invokevirtual(p(Class.class), "getClassLoader", sig(ClassLoader.class));
        block.ldc(type.getName());
        block.invokevirtual(p(ClassLoader.class), "loadClass", sig(Class.class, String.class));
        block.invokestatic(p(JavaTypes.class), "coerceTo", sig(Object.class, Object.class, Class.class));
        block.checkcast(p(type));
    }
}
