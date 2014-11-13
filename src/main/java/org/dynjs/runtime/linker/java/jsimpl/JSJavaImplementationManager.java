package org.dynjs.runtime.linker.java.jsimpl;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import me.qmx.jitescript.internal.org.objectweb.asm.Opcodes;
import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.java.ObjectMethodGenerator;
import org.dynjs.runtime.linker.js.shadow.ShadowObjectLinker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class JSJavaImplementationManager {

    private static AtomicInteger counter = new AtomicInteger();

    private Map<Class<?>, Class<?>> implementations = new HashMap<>();

    private ObjectMethodGenerator objectMethodGenerator = new ObjectMethodGenerator();

    private ShadowObjectLinker shadowLinker;

    public JSJavaImplementationManager(ShadowObjectLinker shadowLinker) {
        this.shadowLinker = shadowLinker;
    }

    public Object getImplementationWrapper(Class<?> targetClass, ExecutionContext context, JSObject implementation) throws Exception {
        Class<?> implClass = getImplementationWrapper(targetClass, context.getClassLoader());

        Constructor<?> ctor = implClass.getConstructor(ExecutionContext.class, JSObject.class);

        JSObject shadow = createShadow(context, implClass, implementation);

        Object implInstance = ctor.newInstance(context, implementation);

        if (shadow != null) {
            this.shadowLinker.putShadowObject(implInstance, shadow);
        }

        return implInstance;
    }

    protected JSObject createShadow(ExecutionContext context, Class<?> implClass, JSObject implementation) {
        DynObject shadow = new DynObject(context.getGlobalContext());
        Method[] methods = implClass.getDeclaredMethods();
        
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

    public Class<?> getImplementationWrapper(Class<?> targetClass, DynamicClassLoader classLoader) {
        Class<?> implClass = this.implementations.get(targetClass);
        if (implClass == null) {
            implClass = createImplementationWrapper(targetClass, classLoader);
            this.implementations.put(targetClass, implClass);
        }

        return implClass;
    }

    private Class<?> createImplementationWrapper(Class<?> targetClass, DynamicClassLoader classLoader) {
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

        CodeBlock codeBlock = new CodeBlock()
            .aload(Arities.THIS)
             // this
            .invokespecial(superClassName, "<init>", sig(void.class))
             // <empty>

            .aload(Arities.THIS)
             // this
            .aload(1)
             // this context
            .putfield(className.replace('.', '/'), "context", ci(ExecutionContext.class))
             // <empty>

            .aload(Arities.THIS)
             // this
            .aload(2)
             // this JSObject
            .putfield(className.replace('.', '/'), "implementation", ci(JSObject.class))
             // <empty>
            .aload(2)
             // JSObject

            .voidreturn();

        jiteClass.defineMethod("<init>", Opcodes.ACC_PUBLIC, sig(void.class, ExecutionContext.class, JSObject.class), codeBlock);

        defineMethods(targetClass, jiteClass, superClass);

        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);

        return classLoader.define(jiteClass.getClassName().replace('/', '.'), bytecode);
    }

    private void defineMethods(Class<?> targetClass, JiteClass jiteClass, Class<?> superClass) {
        for (Method method:  getMethods(targetClass)) {
            int modifiers = method.getModifiers();

            if ((Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) && 
                    !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                defineMethod(method, jiteClass, superClass);
            }
        }
    }

    private void defineMethod(final Method method, final JiteClass jiteClass, final Class<?> superClass) {
        objectMethodGenerator.defineMethod(method, jiteClass, superClass);
    }
    
    private List<Method> getMethods(Class<?> targetClass) {
        List<Method> methods = new LinkedList<Method>();
        methods.addAll(Arrays.asList(targetClass.getMethods()));
        
        return addProtectedMethods(targetClass, methods);
    }

    private List<Method> addProtectedMethods(Class<?> targetClass, List<Method> methods) {
        if (targetClass != null && !targetClass.equals(Object.class)) {
            List<Method> declaredMethods = Collections.unmodifiableList(methods);
            
            for (Method method : targetClass.getDeclaredMethods()) {
                if (Modifier.isProtected(method.getModifiers()) && !alreadyDeclaredMethod(method, declaredMethods)) {
                    methods.add(method);
                }
            }
            
            addProtectedMethods(targetClass.getSuperclass(), methods);            
        }
        return methods;
    }
    
    private boolean alreadyDeclaredMethod(Method method, List<Method> declaredMethods) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Method declaredMethod : declaredMethods) {
            if (method.getName().equals(declaredMethod.getName()) &&
                    Arrays.equals(parameterTypes, declaredMethod.getParameterTypes())) {
                return true;
            }
        }
        return false;
    }
}
