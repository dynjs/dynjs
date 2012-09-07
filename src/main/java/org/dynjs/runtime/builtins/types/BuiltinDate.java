package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class BuiltinDate extends AbstractBuiltinType {

    public BuiltinDate(final GlobalObject globalObject) {
        super(globalObject, "year", "month", "date", "hours", "minutes", "seconds", "ms");
        final DynDate proto = new DynDate(globalObject);
        put(null, "prototype", proto, false);
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynDate(context.getGlobalObject());
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject prototype) {
        prototype.put(null, "constructor", this, false);
        proto
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
