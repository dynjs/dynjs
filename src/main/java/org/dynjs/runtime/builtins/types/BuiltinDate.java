package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.dynjs.runtime.builtins.types.date.prototype.Now;
import org.dynjs.runtime.builtins.types.date.prototype.Parse;

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
        put(null, "now", new Now(globalObject), false);
        put(null, "parse", new Parse(globalObject), false);
        prototype.put(null, "constructor", this, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (args[0] == Types.UNDEFINED) {

        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
