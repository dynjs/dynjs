package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.string.CharAt;
import org.dynjs.runtime.builtins.types.string.DynString;
import org.dynjs.runtime.builtins.types.string.ToString;

public class BuiltinString extends AbstractNativeFunction {

    public BuiltinString(final GlobalObject globalObject) {
        super(globalObject, "value");
        final DynObject proto = new DynObject( globalObject );
        proto.setClassName("String");
        proto.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", BuiltinString.this);
            }
        }, false);
        proto.defineOwnProperty(null, "toString", new PropertyDescriptor() {
            {
                set("Value", new ToString(globalObject));
            }
        }, false);
        proto.defineOwnProperty(null, "valueOf", new PropertyDescriptor() {
            {
                set("Value", new ToString(globalObject));
            }
        }, false);
        proto.defineOwnProperty(null, "charAt", new PropertyDescriptor() {
            {
                set("Value", new CharAt(globalObject));
            }
        }, false);
        defineOwnProperty(null, "prototype", new PropertyDescriptor() {
            {
                set( "Value", proto );
            }
        }, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self != null) {
            if (args[0] != Types.UNDEFINED) {
                PrimitiveDynObject primSelf = (PrimitiveDynObject) self;
                primSelf.setPrimitiveValue(Types.toString(args[0]));
                return primSelf;
            }

        }

        return null;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynString(context.getGlobalObject());
    }

}
