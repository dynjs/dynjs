package org.dynjs.runtime.linker.java;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.ReferenceContext;
import org.dynjs.runtime.linker.BaseDynJSLinker;

import com.headius.invokebinder.Binder;

public class JavaShadowObjectLinker extends BaseDynJSLinker {

    private Map<Object, JSObject> shadows = new WeakHashMap<>();

    public JavaShadowObjectLinker() {

    }

    private JSObject getShadowFor(GlobalObject globalObject, Object object) {
        JSObject shadow = this.shadows.get(object);
        if (shadow == null) {
            shadow = new DynObject(globalObject);
            this.shadows.put(object, shadow);
        }

        return shadow;
    }

    @Override
    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {

        final Object receiver = linkRequest.getReceiver();
        if (isPrimitive(receiver)) {
            return null;
        }
        ReferenceContext context = (ReferenceContext) linkRequest.getArguments()[1];
        JSObject shadow = getShadowFor(context.getContext().getGlobalObject(), receiver);

        Binder binder = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class);
        binder = binder.drop(0);
        binder = binder.insert(0, shadow);
        binder = binder.drop(1);
        binder = binder.convert(Object.class, JSObject.class, String.class);
        binder = binder.insert(1, context.getContext());
        MethodHandle mh = binder.invoke(JSOBJECT_GET);

        try {
            MethodHandle guard = Binder.from(boolean.class, Object.class, ReferenceContext.class, String.class)
                    .drop(1)
                    .insert(2, new Class[] { Object.class }, receiver)
                    .insert(3, linkRequest.getArguments()[2])
                    .invokeStatic(lookup(), JavaShadowObjectLinker.class, "guardShadow");
            return new GuardedInvocation(mh, guard );
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return null;

    }

    public static boolean guardShadow(Object receiver, String name, Object expectedReceiver, String expectedName) {
        return (receiver == expectedReceiver) && (name.equals(expectedName));
    }

    @Override
    protected GuardedInvocation linkSetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        final Object receiver = linkRequest.getReceiver();

        if (isPrimitive(receiver)) {
            return null;
        }

        ReferenceContext refContext = (ReferenceContext) linkRequest.getArguments()[1];
        ExecutionContext context = refContext.getContext();
        Reference ref = refContext.getReference();
        JSObject shadow = getShadowFor(context.getGlobalObject(), receiver);

        Binder binder = Binder.from(desc.getMethodType());
        binder = binder.drop(1);
        binder = binder.insert(1, context);
        binder = binder.insert(4, ref.isStrictReference());
        binder = binder.drop(0);
        binder = binder.insert(0, shadow);
        MethodHandle mh = binder.invoke(JSOBJECT_PUT);
        return new GuardedInvocation(mh, Guards.getIdentityGuard(receiver));
    }

    protected boolean isPrimitive(Object o) {
        return (o instanceof String || o instanceof Boolean || o instanceof Number);
    }
}