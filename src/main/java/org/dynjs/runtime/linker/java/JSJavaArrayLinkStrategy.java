package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.ContextualLinkStrategy;
import org.projectodd.rephract.mop.java.JavaArrayLinkStrategy;

import com.headius.invokebinder.Binder;

public class JSJavaArrayLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private JavaArrayLinkStrategy javaLinkStrategy;

    public JSJavaArrayLinkStrategy(LinkLogger logger) {
        super(ExecutionContext.class, logger);
        this.javaLinkStrategy = new JavaArrayLinkStrategy();
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
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {
        if (receiver instanceof Reference) {
            receiver = ((Reference) receiver).getBase();
            binder = binder.drop(1).filter(0, referenceBaseFilter());
            guardBinder = guardBinder.drop(1).filter(0, referenceBaseFilter());
        }
        return javaLinkStrategy.linkSetProperty(chain, receiver, propName, value, binder, guardBinder);
    }

    public static MethodHandle referenceBaseFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JSJavaArrayLinkStrategy.class, "referenceBaseFilter",
                MethodType.methodType(Object.class, Object.class));
    }

    public static Object referenceBaseFilter(Object obj) {
        if (obj instanceof Reference) {
            return ((Reference) obj).getBase();
        }

        return obj;
    }

}
