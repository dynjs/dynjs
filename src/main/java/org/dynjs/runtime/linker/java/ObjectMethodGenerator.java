package org.dynjs.runtime.linker.java;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;
import org.dynjs.codegen.CodeGeneratingVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.dynjs.runtime.linker.DynJSCoercionMatrix;
import org.projectodd.rephract.java.reflect.CoercionMatrix;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ObjectMethodGenerator {

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

        // <EMPTY>
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
        } else if (returnType == String.class) {
            codeBlock.checkcast(p(String.class));
        } else if (returnType == boolean.class) {
            // result
            codeBlock.invokestatic(p(DynJSCoercionMatrix.class), "coerceToBoolean", sig(boolean.class, Object.class));
        } else if (returnType == Boolean.class) {
            codeBlock.checkcast(p(Boolean.class));
        } else if ( returnType != void.class) {
            codeBlock.checkcast(p(returnType));
        }
        // result
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

    protected void callJavascriptImplementation(Method method, JiteClass jiteClass, CodeBlock block, LabelNode noImpl) {
        final Class<?>[] params = method.getParameterTypes();

        LabelNode internalNoImpl = new LabelNode();
        LabelNode internalHasImpl = new LabelNode();

        block.aload(CodeGeneratingVisitor.Arities.THIS);
        // this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "implementation", ci(JSObject.class));
        // obj

        block.aload(CodeGeneratingVisitor.Arities.THIS);
        // obj this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "context", ci(ExecutionContext.class));
        // obj context

        block.ldc(method.getName());
        // obj context name

        block.invokeinterface(p(JSObject.class), "get", sig(Object.class, ExecutionContext.class, String.class));
        // fn
        block.dup();
        // fn fn
        block.getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
        // fn fn undef

        block.if_acmpeq(internalNoImpl);
        // fn

        block.go_to(internalHasImpl);

        block.label(internalNoImpl);
        // fn
        block.pop();
        // empty
        block.go_to(noImpl);
        // EXIT

        block.label(internalHasImpl);
        // fn
        block.aload(CodeGeneratingVisitor.Arities.THIS);
        // fn this
        block.getfield(jiteClass.getClassName().replace('.', '/'), "context", ci(ExecutionContext.class));
        // fn context

        block.aload(CodeGeneratingVisitor.Arities.THIS);
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
        block.invokedynamic("dyn:call", sig(Object.class, Object.class, ExecutionContext.class, Object.class, Object[].class),
                DynJSBootstrapper.HANDLE, DynJSBootstrapper.ARGS);
        // result
        /*
        if (returnType == Void.TYPE) {
            // aconst_null();
            handleDefaultReturnValue(returnType, block);
        }
        */
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


            block.aload(CodeGeneratingVisitor.Arities.THIS);
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


}
