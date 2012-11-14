package org.dynjs.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.JSFunction;

import com.headius.invokebinder.Binder;

public abstract class AbstractDynJSLinker implements GuardingDynamicLinker {

    private static final MethodHandle THROWER;

    static {
        try {
            THROWER = MethodHandles.throwException(Object.class, ThrowException.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected MethodHandle getThrower(Throwable t, MethodType type) {
        return Binder.from( type )
                .drop(0, type.parameterCount() )
                .insert(0, t)
                .invoke( THROWER );
    }
    
    protected MethodHandle getFunctionGuard(MethodType type) {
        return Guards.isInstance(JSFunction.class, type);
    }
    
}
