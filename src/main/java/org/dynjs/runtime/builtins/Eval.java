package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

public class Eval implements Function {
    @Override
    public void setContext(DynThreadContext context) {

    }

    @Override
    public Object call(DynThreadContext context, Object[] arguments) {
        if (arguments.length == 1 && arguments[0] instanceof String) {
            DynJS runtime = context.getRuntime();
            runtime.eval(context, (String) arguments[0]);
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object resolve(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void define(String property, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
