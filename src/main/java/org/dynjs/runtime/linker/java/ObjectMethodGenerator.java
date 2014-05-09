package org.dynjs.runtime.linker.java;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;
import org.dynjs.runtime.linker.DynJSCoercionMatrix;
import org.projectodd.rephract.java.reflect.CoercionMatrix;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ObjectMethodGenerator extends MethodGenerator {
    
    public void defineMethod(final Method method, final JiteClass jiteClass, final Class<?> superClass) {

        if (method.getName().equals("equals") || method.getName().equals("hashCode") || method.getName().equals("toString")) {
            return;
        }
        final Class<?>[] params = method.getParameterTypes();
        final Class<?>[] signature = new Class<?>[params.length + 1];
        final Class<?> returnType = method.getReturnType();

        for (int i = 1; i < params.length + 1; ++i) {
            signature[i] = params[i - 1];
        }

        signature[0] = returnType;

        LabelNode noImpl = new LabelNode();
        LabelNode complete = new LabelNode();
        CodeBlock codeBlock = new CodeBlock();
        callJavascriptImplementation(method, jiteClass, codeBlock, noImpl);
        // result
        // Coerce our JavaScript values to the appropriate return type
        if (returnType == int.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveInteger", sig(int.class, Number.class));
        } else if (returnType == long.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveLong", sig(long.class, Number.class));
        } else if (returnType == short.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveShort", sig(short.class, Number.class));
        } else if (returnType == float.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveFloat", sig(float.class, Number.class));
        } else if (returnType == double.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveDouble", sig(double.class, Number.class));
        } else if (returnType == Integer.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToInteger", sig(Integer.class, Number.class));
        } else if (returnType == Long.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToLong", sig(Long.class, Number.class));
        } else if (returnType == Short.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToShort", sig(Short.class, Number.class));
        } else if (returnType == Float.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToFloat", sig(Float.class, Number.class));
        } else if (returnType == Double.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToDouble", sig(Double.class, Number.class));
        } else if (returnType == char.class) {
            codeBlock.checkcast(p(String.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "stringToPrimitiveCharacter", sig(char.class, String.class));
        } else if (returnType == Character.class) {
            codeBlock.checkcast(p(String.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "stringToCharacter", sig(Character.class, String.class));
        } else if (returnType == byte.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(CoercionMatrix.class), "numberToPrimitiveByte", sig(byte.class, Number.class));
        } else if (returnType == Byte.class) {
            codeBlock.checkcast(p(Number.class));
            codeBlock.invokestatic(p(DynJSCoercionMatrix.class), "numberToByte", sig(Byte.class, Number.class));
        }
        codeBlock.go_to(complete);

        codeBlock.label(noImpl);
        // empty
        callSuperImplementation(method, superClass, codeBlock);
        // result

        codeBlock.label(complete);
        // result
        if (returnType == Void.TYPE) {
            codeBlock.voidreturn();
        } else if (returnType == int.class || returnType == short.class ||
                returnType == boolean.class || returnType == char.class ||
                returnType == byte.class) {
            codeBlock.ireturn();
        } else if (returnType == long.class) {
            codeBlock.lreturn();
        } else if (returnType == float.class) {
            codeBlock.freturn();
        } else if (returnType == double.class) {
            codeBlock.dreturn();
        } else {
            codeBlock.areturn();
        }
        jiteClass.defineMethod(method.getName(), method.getModifiers() & ~Modifier.ABSTRACT, sig(signature), codeBlock);
    }

    @Override
    protected void handleDefaultReturnValue(Class<?> returnType, CodeBlock block) {
        if (returnType == Void.TYPE || Object.class.isAssignableFrom(returnType)) {
            block.aconst_null();
        } else if (returnType == int.class || returnType == short.class ||
                returnType == boolean.class || returnType == char.class ||
                returnType == byte.class) {
            block.ldc(0);
        } else if (returnType == long.class) {
            block.ldc(0L);
        } else if (returnType == float.class) {
            block.ldc(0.0f);
        } else if (returnType == double.class) {
            block.ldc(0.0d);
        }
    }


}
