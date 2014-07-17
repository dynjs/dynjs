package org.dynjs.runtime.linker.js.environment;

import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.js.ReferenceBaseFilter;
import org.dynjs.runtime.linker.js.ReferenceStrictnessFilter;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class EnvironmentPropertyGetLink extends SmartLink implements Guard {

    public EnvironmentPropertyGetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object context, String propertyName) {
        boolean result = (receiver instanceof Reference) && (((Reference) receiver).getBase() instanceof EnvironmentRecord);
        //System.out.println( "-- TEST " + this + " >> " + result + " // " + receiver );
        return result;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(EnvironmentPropertyGetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return builder
                //.convert(Object.class, Reference.class, ExecutionContext.class, String.class)
                .permute(0, 1, 2, 0)
                .convert(Object.class, Reference.class, ExecutionContext.class, String.class, Reference.class)
                .filter(0, ReferenceBaseFilter.INSTANCE)
                .filter(3, ReferenceStrictnessFilter.INSTANCE)
                .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, boolean.class)
                .invoke(lookup().findVirtual(EnvironmentRecord.class, "getBindingValue", methodType(Object.class, ExecutionContext.class, String.class, boolean.class)))
                .target();

    }

}
