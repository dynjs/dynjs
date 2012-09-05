package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.string.CharAt;
import org.dynjs.runtime.builtins.types.string.DynString;
import org.dynjs.runtime.builtins.types.string.ToString;

public class BuiltinString extends AbstractBuiltinType {

    public BuiltinString(final GlobalObject globalObject) {
        super(globalObject, "value");

        final DynObject proto = new DynObject(globalObject);
        proto.setClassName("String");
        put(null, "prototype", proto, false);
    }

    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        proto.put(null, "constructor", this, false);
        proto.put(null, "toString", new ToString(globalObject), false);
        proto.put(null, "valueOf", new ToString(globalObject), false);
        proto.put(null, "charAt", new CharAt(globalObject), false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self != Types.UNDEFINED) {
            // Constructor
            if (args[0] != Types.UNDEFINED) {
                PrimitiveDynObject primSelf = (PrimitiveDynObject) self;
                primSelf.setPrimitiveValue(Types.toString(context, args[0]));
                return primSelf;
            } else {
                return self;
            }
        } else {
            // As function
            return Types.toString(context, args[0]);
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynString(context.getGlobalObject());
    }

}
