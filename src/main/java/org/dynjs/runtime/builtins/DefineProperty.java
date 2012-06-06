package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynProperty;
import org.dynjs.runtime.DynThreadContext;

public class DefineProperty implements Function {
    @Override
    public Object call(Object self, DynThreadContext context, Object[] arguments) {
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
    public String[] getArguments() {
        return new String[]{"object", "name", "desc"};
    }

}
