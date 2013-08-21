package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;

import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.linker.js.ShadowObjectLinkStrategy;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JSJavaImplementationLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private JSJavaImplementationManager manager;

    public JSJavaImplementationLinkStrategy(JSJavaImplementationManager manager, LinkLogger logger) {
        super(ExecutionContext.class, logger);
        this.manager = manager;
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (!(receiver instanceof Class<?>)) {
            return chain.nextStrategy();
        }

        // This is designed to shadow DynObjects as Hashes, but we don't want to
        // do that for DynArrays. We'll leave that up to the array coercion logic
        if (args.length == 1 && args[0] instanceof JSObject && !(args[0] instanceof DynArray)) {

            binder = binder.spread(JSObject.class)
                    .convert(Object.class, Class.class, ExecutionContext.class, JSObject.class);

            MethodHandle guard = getConstructGuard((Class<?>) receiver, guardBinder);

            return new StrategicLink(makeImplementation(binder), guard);
        }

        return chain.nextStrategy();

    }

    private MethodHandle makeImplementation(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder
                .insert(0, this.manager)
                .invokeStatic(lookup(), JSJavaImplementationLinkStrategy.class, "makeImplementation");
    }

    public static Object makeImplementation(JSJavaImplementationManager manager, Class<?> targetClass, ExecutionContext context, JSObject implementation) throws Exception {
        return manager.getImplementationWrapper(targetClass, context, implementation);
    }

    private MethodHandle getConstructGuard(Class<?> targetClass, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder
                .drop(0)
                .insert(2, targetClass)
                .invokeStatic(lookup(), JSJavaImplementationLinkStrategy.class, "constructGuard");
    }

    public static boolean constructGuard(Object targetClass, Object[] args, Class<?> expectedTargetClass) {
        if (targetClass != expectedTargetClass) {
            return false;
        }

        if (args.length != 1) {
            return false;
        }

        if (!(args[0] instanceof JSObject)) {
            return false;
        }
        return true;
    }

}
