package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.linker.js.JavascriptObjectLinkStrategy;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;
import org.projectodd.linkfusion.mop.java.JavaLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavascriptJavaLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private JavaLinkStrategy javaLinkStrategy;

    public JavascriptJavaLinkStrategy() {
        super(ExecutionContext.class);
        this.javaLinkStrategy = new JavaLinkStrategy();
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        return javaLinkStrategy.linkGetProperty(chain, receiver, propName, binder, guardBinder);
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {
        return javaLinkStrategy.linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        return javaLinkStrategy.linkGetMethod(chain, receiver, methodName, binder, guardBinder);
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        Object[] linkArgs = chain.getRequest().arguments();

        if (linkArgs.length >= 2 && linkArgs[1] instanceof ExecutionContext) {
            ExecutionContext context = (ExecutionContext) linkArgs[1];
            if (context.getPendingConstructorCount() > 0) {
                context.decrementPendingConstructorCount();
                binder = binder.filter(0, dereferencedValueFilter());
                guardBinder = guardBinder.filter(0, dereferencedValueFilter());
                
                binder = binder.drop(1,2);
                guardBinder = guardBinder.drop(1,2);
                return javaLinkStrategy.linkConstruct(chain, dereferencedValueFilter(receiver), args, binder, guardBinder);
            }
        }
        return javaLinkStrategy.linkCall(chain, receiver, self, args, binder, guardBinder);
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        binder = binder.drop(1);
        guardBinder = guardBinder.drop(1);
        return javaLinkStrategy.linkConstruct(chain, receiver, args, binder, guardBinder);
    }

    public static MethodHandle dereferencedValueFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptJavaLinkStrategy.class, "dereferencedValueFilter",
                MethodType.methodType(Object.class, Object.class));
    }

    public static Object dereferencedValueFilter(Object deref) {
        if (deref instanceof DereferencedReference) {
            return ((DereferencedReference) deref).getValue();
        }
        return deref;
    }
}
