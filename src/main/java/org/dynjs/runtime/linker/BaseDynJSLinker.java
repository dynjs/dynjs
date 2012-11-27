package org.dynjs.runtime.linker;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.CallSiteDescriptorFactory;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ReferenceContext;

import com.headius.invokebinder.Binder;

public class BaseDynJSLinker extends AbstractDynJSLinker {
    
    protected static final MethodHandle CONTEXT_FILTER;
    protected static final MethodHandle REFERENCE_STRICTNESS_FILTER;
    protected static final MethodHandle RETURN_FILTER;
    protected static final MethodHandle TO_JAVA_FILTER;


    protected static MethodHandle JSOBJECT_PUT;
    protected static MethodHandle JSOBJECT_GET;
    
    protected static MethodHandle ENVIRONMENTRECORD_SET_MUTABLE_BINDING;

    static {
        try {
            
            CONTEXT_FILTER = Binder.from(ExecutionContext.class, ReferenceContext.class)
                    .invokeStatic(lookup(), Filters.class, "filterContext" );
            
            REFERENCE_STRICTNESS_FILTER = Binder.from(boolean.class, ReferenceContext.class)
                    .invokeStatic(lookup(), Filters.class, "filterReferenceStrictness" );
            
            RETURN_FILTER = Binder.from(Object.class, Object.class)
                    .invokeStatic(lookup(), Filters.class, "filterReturn" );
            
            TO_JAVA_FILTER = Binder.from(Object.class, ExecutionContext.class, Object.class)
                    .invokeStatic(lookup(), Filters.class, "filterToJava" );
            
            
            JSOBJECT_PUT = Binder.from(Object.class, Object.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");
            
            JSOBJECT_GET = Binder.from(Object.class, Object.class, ExecutionContext.class, String.class)
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                    .invokeVirtual(lookup(), "get");
            
            ENVIRONMENTRECORD_SET_MUTABLE_BINDING = Binder.from(Object.class, Object.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "setMutableBinding");
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        GuardedInvocation result = null;
        if (desc.getNameToken(0).equals("dyn")) {
            List<String> ops = CallSiteDescriptorFactory.tokenizeOperators(desc);

            for (String op : ops) {
                if (op.equals("call")) {
                    result = linkCall(linkRequest, desc);
                }
                if (op.equals("new")) {
                    result = linkNew(linkRequest, desc);
                }
                if (op.equals("getProp")) {
                    result = linkGetProp(linkRequest, desc);
                }
                if (op.equals("getMethod")) {
                    result = linkGetMethod(linkRequest, desc);
                }
                if (op.equals("setProp")) {
                    result = linkSetProp(linkRequest, desc);
                }
                if (result != null) {
                    break;
                }
            }
        }

        return result;
    }

    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

    protected GuardedInvocation linkGetMethod(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

    protected GuardedInvocation linkSetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

}
