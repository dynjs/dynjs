package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.projectodd.linkfusion.LinkLogger;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavaNullReplacingLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private static MethodHandle FILTER_HANDLE;

    static {
        try {
            FILTER_HANDLE = lookup().findStatic(JavaNullReplacingLinkStrategy.class, "filter", MethodType.methodType(Object.class, Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public JavaNullReplacingLinkStrategy(LinkLogger logger) {
        super(ExecutionContext.class, logger);
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        StrategicLink link = super.linkGetProperty(chain, receiver, propName, binder, guardBinder);
        if (link != null) {
            MethodHandle target = link.getTarget();
            return new StrategicLink(Binder.from(target.type()).filterReturn(FILTER_HANDLE).invoke(target), link.getGuard());
        }
        return null;
    }

    @Override
    public StrategicLink linkGetMethod(StrategyChain chain, Object receiver, String methodName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        StrategicLink link = super.linkGetMethod(chain, receiver, methodName, binder, guardBinder);
        if (link != null) {
            MethodHandle target = link.getTarget();
            return new StrategicLink(Binder.from(target.type()).filterReturn(FILTER_HANDLE).invoke(target), link.getGuard());
        }
        return null;
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        // TODO Auto-generated method stub
        StrategicLink link = super.linkCall(chain, receiver, self, args, binder, guardBinder);
        if (link != null) {
            MethodHandle target = link.getTarget();
            return new StrategicLink(Binder.from(target.type()).filterReturn(FILTER_HANDLE).invoke(target), link.getGuard());
        }
        return null;
    }

    public static Object filter(Object value) {
        if (value == null) {
            return Types.UNDEFINED;
        }
        return value;
    }

}
