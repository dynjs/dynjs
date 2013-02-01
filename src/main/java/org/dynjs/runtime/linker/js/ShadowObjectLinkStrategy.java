package org.dynjs.runtime.linker.js;

import static org.dynjs.runtime.linker.LinkerUtils.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.WeakHashMap;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class ShadowObjectLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    private Map<Object, JSObject> shadowObjects = new WeakHashMap<>();

    public ShadowObjectLinkStrategy(LinkLogger logger) {
        super(ExecutionContext.class, logger);
    }

    protected JSObject getShadowObject(Object primary) {
        return getShadowObject(primary, true);
    }
    
    protected JSObject getShadowObject(Object primary, boolean create) {
        JSObject shadow = this.shadowObjects.get(primary);
        if (shadow == null && create) {
            shadow = new DynObject(null);
            this.shadowObjects.put(primary, shadow);
        }
        return shadow;
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (isJavascriptObjectReference(receiver) || isJavascriptEnvironmnetReference(receiver) || isJavascriptUndefinedReference(receiver)) {
            return chain.nextStrategy();
        }
        
        if ( receiver instanceof Reference ) {
            Object primary = ((Reference) receiver).getBase();
            JSObject shadow = getShadowObject(primary, false);
            
            if ( shadow == null ) {
                return chain.nextStrategy();
            }
            
            MethodHandle handle = binder
                    .drop(0)
                    .insert(0, shadow)
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                    .invokeVirtual(lookup(), "get");

            MethodHandle guard = shadowObjectGuard(primary, guardBinder);
            return new StrategicLink(handle, guard);
        }

        return null;
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {

        if (isJavascriptObjectReference(receiver) || isJavascriptEnvironmnetReference(receiver) || isJavascriptUndefinedReference(receiver)) {
            return chain.nextStrategy();
        }

        if (receiver instanceof Reference) {
            Object primary = ((Reference) receiver).getBase();
            JSObject shadow = getShadowObject(primary);

            MethodHandle handle = binder
                    .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                    .permute(0, 1, 2, 3, 0)
                    .drop(0)
                    .insert(0,shadow)
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");

            MethodHandle guard = shadowObjectGuard(primary, guardBinder);
            return new StrategicLink(handle, guard);
        }
        
        return chain.nextStrategy();

    }
    
    public static MethodHandle shadowObjectGuard(Object expectedPrimary, Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return identityGuard(expectedPrimary, binder.filter(0, referenceBaseFilter() ) );
    }

}
