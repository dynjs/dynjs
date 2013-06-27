package org.dynjs.runtime.linker.java;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ThreadCompilationManager;
import org.dynjs.runtime.linker.js.ShadowObjectLinkStrategy;
import org.objectweb.asm.Opcodes;

public class JSJavaImplementationManager {

    private static AtomicInteger counter = new AtomicInteger();

    private Map<Class<?>, Class<?>> implementations = new HashMap<>();

    private BooleanMethodGenerator booleanMethodGenerator = new BooleanMethodGenerator();
    private ObjectMethodGenerator objectMethodGenerator = new ObjectMethodGenerator();

    private ShadowObjectLinkStrategy shadowLinker;

    public JSJavaImplementationManager(ShadowObjectLinkStrategy shadowLinker) {
        this.shadowLinker = shadowLinker;
    }

    public Object getImplementationWrapper(Class<?> targetClass, ExecutionContext context, JSObject implementation) throws Exception {
        Class<?> implClass = getImplementationWrapper(targetClass);

        Constructor<?> ctor = implClass.getConstructor(ExecutionContext.class, JSObject.class);

        JSObject shadow = createShadow(context, implClass, implementation);

        Object implInstance = ctor.newInstance(context, implementation);

        if (shadow != null) {
            this.shadowLinker.putShadowObject(implInstance, shadow);
        }

        return implInstance;
    }

    protected JSObject createShadow(ExecutionContext context, Class<?> implClass, JSObject implementation) {
        DynObject shadow = new DynObject(context.getGlobalObject());
        Method[] methods = implClass.getMethods();

        boolean shadowed = false;

        List<String> propNames = implementation.getOwnPropertyNames().toList();
        props: for (String propName : propNames) {
            for (Method m : methods) {
                if (m.getName().equals(propName)) {
                    continue props;
                }
            }
            Object val = implementation.get(context, propName);
            if (val instanceof JSFunction) {
                shadowed = true;
                shadow.put(propName, val);
                implementation.delete(context, propName, false);
            }
        }
        if (shadowed) {
            return shadow;
        }

        return null;
    }

    public Class<?> getImplementationWrapper(Class<?> targetClass) {
        Class<?> implClass = this.implementations.get(targetClass);
        if (implClass == null) {
            implClass = createImplementationWrapper(targetClass);
            this.implementations.put(targetClass, implClass);
        }

        return implClass;
    }

    private Class<?> createImplementationWrapper(Class<?> targetClass) {
        final String className = "org/dynjs/gen/impl/" + targetClass.getSimpleName() + "JS_" + counter.getAndIncrement();

        final Class<?> superClass = (targetClass.isInterface() ? Object.class : targetClass);
        final String superClassName = p(superClass);
        String[] interfaces = null;

        if (targetClass.isInterface()) {
            interfaces = new String[] { p(targetClass) };
        } else {
            interfaces = new String[] {};
        }

        JiteClass jiteClass = new JiteClass(className, superClassName, interfaces);

        jiteClass.defineField("context", Opcodes.ACC_PRIVATE, ci(ExecutionContext.class), null);
        jiteClass.defineField("implementation", Opcodes.ACC_PRIVATE, ci(JSObject.class), null);

        jiteClass.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, ExecutionContext.class, JSObject.class),
                new CodeBlock() {
                    {
                        aload(Arities.THIS);
                        invokespecial(superClassName, "<init>", sig(void.class));

                        aload(Arities.THIS);
                        aload(1);
                        putfield(className.replace('.', '/'), "context", ci(ExecutionContext.class));

                        aload(Arities.THIS);
                        aload(2);
                        putfield(className.replace('.', '/'), "implementation", ci(JSObject.class));
                        aload(2);

                        voidreturn();
                    }
                });

        defineMethods(targetClass, jiteClass, superClass);

        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);

        //DynamicClassLoader cl = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
        DynamicClassLoader cl = ThreadCompilationManager.currentClassLoader();

        return cl.define(jiteClass.getClassName().replace('/', '.'), bytecode);
    }

    private void defineMethods(Class<?> targetClass, JiteClass jiteClass, Class<?> superClass) {
        Method[] methods = targetClass.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();

            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                defineMethod(methods[i], jiteClass, superClass);
            }
        }
    }

    private void defineMethod(final Method method, final JiteClass jiteClass, final Class<?> superClass) {

        Class<?> returnType = method.getReturnType();
        if (returnType == Boolean.TYPE) {
            booleanMethodGenerator.defineMethod(method, jiteClass, superClass);
        } else {
            objectMethodGenerator.defineMethod(method, jiteClass, superClass);
        }
    }
}
