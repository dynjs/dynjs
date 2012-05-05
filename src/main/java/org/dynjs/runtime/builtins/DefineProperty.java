package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynProperty;
import org.dynjs.runtime.DynThreadContext;

public class DefineProperty implements Function {
    @Override
    public Object call(DynThreadContext context, Object[] arguments) {
        if (arguments.length == 3) {
            DynObject object = (DynObject) arguments[0];
            String name = (String) arguments[1];
            DynProperty property = new DynProperty();
            property.setAttribute("value", arguments[2]);
            object.setProperty(name, property);
            return object;
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public void setContext(DynThreadContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
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
