package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;
import org.projectodd.linkfusion.mop.java.Resolver;
import org.projectodd.linkfusion.mop.java.ResolverManager;

import com.headius.invokebinder.Binder;

public class JSJavaInterfaceLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private ResolverManager manager;

    public JSJavaInterfaceLinkStrategy() {
        this(new ResolverManager());
    }

    public JSJavaInterfaceLinkStrategy(ResolverManager manager) {
        super(ExecutionContext.class);
        this.manager = manager;
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (!(receiver instanceof Class<?>)) {
            return chain.nextStrategy();
        }

        if (((Class<?>) receiver).isInterface()) {
            if (args.length == 1 && args[0] instanceof JSObject) {

                binder = binder.spread(JSObject.class)
                        .convert(Object.class, Class.class, ExecutionContext.class, JSObject.class);

                MethodHandle guard = getConstructGuard((Class<?>) receiver, guardBinder);

                return new StrategicLink(makeImplementation(binder), guard);
            }
        }

        return chain.nextStrategy();

    }

    private MethodHandle makeImplementation(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.invokeStatic(lookup(), JSJavaInterfaceLinkStrategy.class, "makeImplementation");
    }

    public static Object makeImplementation(Class<?> targetClass, ExecutionContext context, JSObject implementation) {
        Object proxy = Proxy.newProxyInstance(context.getClass().getClassLoader(), new Class<?>[] { targetClass }, new JSInvocationHandler(context, implementation));
        return proxy;
    }

    private MethodHandle getConstructGuard(Class<?> targetClass, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder
                .drop(0)
                .insert(2, targetClass)
                .invokeStatic(lookup(), JSJavaInterfaceLinkStrategy.class, "constructGuard");
    }

    public static boolean constructGuard(Object targetClass, Object[] args, Class<?> expectedTargetClass) {
        if (targetClass != expectedTargetClass) {
            return false;
        }
        
        if ( args.length != 1 ) {
            return false;
        }
        
        if ( ! ( args[0] instanceof JSObject ) ) {
            return false;
        }
        return true;
    }

    private Resolver getResolver(Class<?> targetClass) {
        return this.manager.getResolver(targetClass);
    }

}
