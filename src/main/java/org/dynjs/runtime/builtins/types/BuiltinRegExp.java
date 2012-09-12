package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.dynjs.runtime.builtins.types.regexp.prototype.Exec;

public class BuiltinRegExp extends AbstractBuiltinType {

    public BuiltinRegExp(final GlobalObject globalObject) {
        super(globalObject, "pattern", "flags");

        DynRegExp proto = new DynRegExp(globalObject, "", "");
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        proto.put(null, "constructor", this, false);
        proto.put(null, "exec", new Exec(globalObject), false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {

        if (args[0] instanceof JSObject && ((JSObject) args[0]).getClassName().equals("RegExp")) {
            return args[0];
        }

        if (self == Types.UNDEFINED) {
            return newRegExp(context, Types.toString( context, args[0]), Types.toString( context, args[1]) );
        } else {
            String pattern = Types.toString(context, args[0]);
            String flags = null;

            if (args[1] != Types.UNDEFINED) {
                flags = Types.toString(context, args[1]);
            }

            ((DynRegExp) self).setPatternAndFlags(pattern, flags);

            return self;
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynRegExp(context.getGlobalObject());
    }

    public static DynRegExp newRegExp(ExecutionContext context, String pattern, String flags) {
        BuiltinRegExp ctor = (BuiltinRegExp) context.getGlobalObject().get(context, "RegExp");
        return (DynRegExp) context.construct(ctor, pattern, flags);
    }

}