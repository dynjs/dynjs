package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;
import org.projectodd.linkfusion.mop.java.JavaInstanceLinkStrategy;
import org.projectodd.linkfusion.mop.java.ResolverManager;

import com.headius.invokebinder.Binder;

public class JSJavaInstanceLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private JavaInstanceLinkStrategy javaLinkStrategy;

    public JSJavaInstanceLinkStrategy(ResolverManager manager) {
        super(ExecutionContext.class);
        this.javaLinkStrategy = new JavaInstanceLinkStrategy(manager);
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        if (receiver instanceof Reference) {
            receiver = ((Reference) receiver).getBase();
            binder = binder.drop(1).filter(0, referenceBaseFilter());
            guardBinder = guardBinder.drop(1).filter(0, referenceBaseFilter());
        }
        return javaLinkStrategy.linkGetProperty(chain, receiver, propName, binder, guardBinder);
    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (receiver instanceof Reference) {
            receiver = ((Reference) receiver).getBase();
            binder = binder.drop(1).filter(0, referenceBaseFilter());
            guardBinder = guardBinder.drop(1).filter(0, referenceBaseFilter());
        }
        return javaLinkStrategy.linkGetMethod(chain, receiver, methodName, binder, guardBinder);
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {
        return javaLinkStrategy.linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        binder = binder.drop(1);
        guardBinder = guardBinder.drop(1);
        
        binder = binder.filter(0, dereferencedValueFilter() );
        guardBinder = guardBinder.filter(0, dereferencedValueFilter() );
        return javaLinkStrategy.linkCall(chain, dereferencedValueFilter( receiver), self, args, binder, guardBinder);
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        binder = binder.drop(1);
        guardBinder = guardBinder.drop(1);
        return javaLinkStrategy.linkConstruct(chain, receiver, args, binder, guardBinder);
    }

    public static MethodHandle dereferencedValueFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JSJavaInstanceLinkStrategy.class, "dereferencedValueFilter",
                MethodType.methodType(Object.class, Object.class));
    }

    public static Object dereferencedValueFilter(Object deref) {
        if (deref instanceof DereferencedReference) {
            return ((DereferencedReference) deref).getValue();
        }
        return deref;
    }

    public static MethodHandle referenceBaseFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JSJavaInstanceLinkStrategy.class, "referenceBaseFilter",
                MethodType.methodType(Object.class, Object.class));
    }

    public static Object referenceBaseFilter(Object obj) {
        if (obj instanceof Reference) {
            return ((Reference) obj).getBase();
        }

        return obj;
    }

}
