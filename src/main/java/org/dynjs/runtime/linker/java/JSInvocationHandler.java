package org.dynjs.runtime.linker.java;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JSInvocationHandler implements InvocationHandler {

    private ExecutionContext context;
    private JSObject implementation;

    public JSInvocationHandler(ExecutionContext context, JSObject implementation) {
        this.context = context;
        this.implementation = implementation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();

        Object function = this.implementation.get(this.context, name);

        if (!(function instanceof JSFunction)) {
            return Types.UNDEFINED;
        }

        if (args == null) {
            return this.context.call((JSFunction) function, proxy);
        } else {
            return this.context.call((JSFunction) function, proxy, args);
        }

    }

}
