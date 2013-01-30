package org.dynjs.runtime.linker;

import java.lang.invoke.CallSite;

import org.dynjs.runtime.ExecutionContext;
import org.projectodd.rephract.FusionLinker;
import org.projectodd.rephract.mop.MetaObjectProtocolLinkStrategy;

public class InterpretingInvokeDynamicHandler {

    private FusionLinker linker;

    private CallSite get;
    private CallSite set;

    private CallSite call;
    private CallSite construct;

    public InterpretingInvokeDynamicHandler(FusionLinker linker) throws Throwable {
        this.linker = linker;
        this.get = linker.bootstrap("fusion:getProperty|getMethod", Object.class, Object.class, ExecutionContext.class, String.class);
        this.set = linker.bootstrap("fusion:setProperty", void.class, Object.class, ExecutionContext.class, String.class, Object.class);
        this.call = linker.bootstrap("fusion:call", Object.class, Object.class, ExecutionContext.class, Object.class, Object[].class);
        this.construct = linker.bootstrap("fusion:construct", Object.class, Object.class, ExecutionContext.class, Object[].class);
    }

    public void addLinkStrategy(MetaObjectProtocolLinkStrategy linkStrategy) {
        this.linker.addLinkStrategy(linkStrategy);
    }

    public Object get(Object object, ExecutionContext context, String propertyName) throws Throwable {
        return this.get.getTarget().invoke(object, context, propertyName);
    }

    public void set(Object object, ExecutionContext context, String propertyName, Object value) throws Throwable {
        this.set.getTarget().invoke(object, context, propertyName, value);
    }

    public Object call(Object method, ExecutionContext context, Object self, Object... args) throws Throwable {
        return this.call.getTarget().invoke(method, context, self, args);
    }
    
    public Object construct(Object method, ExecutionContext context, Object... args) throws Throwable {
        return this.construct.getTarget().invoke(method, context, args);
    }

}
