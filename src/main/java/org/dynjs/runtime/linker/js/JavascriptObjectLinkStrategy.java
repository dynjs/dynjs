package org.dynjs.runtime.linker.js;

import static org.dynjs.runtime.linker.LinkerUtils.*;

import java.lang.invoke.MethodHandle;

import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.projectodd.linkfusion.StrategicLink;
import org.projectodd.linkfusion.StrategyChain;
import org.projectodd.linkfusion.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavascriptObjectLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    public JavascriptObjectLinkStrategy() {
        super(ExecutionContext.class);
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        //System.err.println("jsobj: link getprop: " + receiver + " // " + propName);

        if (isJavascriptObjectReference(receiver)) {
            MethodHandle handle = binder
                    .filter(0, referenceBaseFilter())
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                    .invokeVirtual(lookup(), "get");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .convert(Object.class, Reference.class, ExecutionContext.class, String.class )
                    .permute(0, 1, 2, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(3, referenceStrictnessFilter())
                    .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, boolean.class)
                    .invokeVirtual(lookup(), "getBindingValue");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {

        //System.err.println("jsobj: link setprop: " + receiver + " // " + propName);

        if (isJavascriptObjectReference(receiver)) {
            MethodHandle handle = binder
                    .convert( void.class, Reference.class, ExecutionContext.class, String.class, Object.class )
                    .permute(0, 1, 2, 3, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .convert( void.class, Reference.class, ExecutionContext.class, String.class, Object.class )
                    .permute(0, 1, 2, 3, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, EnvironmentRecord.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "setMutableBinding");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }
        
        if ( isJavascriptUndefinedReference(receiver)) {
            MethodHandle handle = binder
                    .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                    .permute(1, 1, 2, 3, 0)
                    .filter(0, globalObjectFilter() )
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        //System.err.println("jsobj: link call: " + receiver.getClass() + " // " + receiver );
        if (isJavascriptObjectReference(receiver)) {
            MethodHandle handle = binder
                    .permute(1, 0, 2, 3)
                    .convert(Object.class, ExecutionContext.class, Reference.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .permute(1, 0, 2, 3)
                    .convert(Object.class, ExecutionContext.class, Reference.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSFunction) {
            MethodHandle handle = binder
                    .permute(1, 0, 2, 3)
                    .convert(Object.class, ExecutionContext.class, JSFunction.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            MethodHandle guard = getReceiverClassGuard(JSFunction.class, guardBinder);

            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {
        //System.err.println("jsobj: link construct: " + receiver.getClass());
        if (isJavascriptObjectReference(receiver)) {
            System.err.println("is func");
            MethodHandle handle = binder
                    .permute(1, 0, 2)
                    .convert(Object.class, ExecutionContext.class, Reference.class, Object[].class)
                    .invokeVirtual(lookup(), "construct");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .permute(1, 0, 2)
                    .convert(Object.class, ExecutionContext.class, Reference.class, Object[].class)
                    .invokeVirtual(lookup(), "construct");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSFunction) {
            MethodHandle handle = binder
                    .permute(1, 0, 2)
                    .convert(Object.class, ExecutionContext.class, JSFunction.class, Object[].class)
                    .invokeVirtual(lookup(), "construct");

            MethodHandle guard = getReceiverClassGuard(JSFunction.class, guardBinder);

            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    /*
     * @Override
     * public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
     * IllegalAccessException {
     * // TODO Auto-generated method stub
     * return super.linkConstruct(chain, receiver, args, binder, guardBinder);
     * }
     */

}
