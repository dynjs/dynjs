package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.array.prototype.Join;
import org.dynjs.runtime.builtins.types.array.prototype.ToString;

public class BuiltinArray extends AbstractNativeFunction {

    public BuiltinArray(final GlobalObject globalObject) {
        super(globalObject);

        final DynArray proto = new DynArray();
        proto.put(null, "constructor", this, false );
        proto.put(null, "toString", new ToString( globalObject ), false );
        proto.put( null, "join", new Join(globalObject), false );
        put(null, "prototype", proto, false );
        
        setPrototype( globalObject.getPrototypeFor( "Function" ));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        DynArray arraySelf = (DynArray) self;

        if (self != Types.UNDEFINED) {
            if (args.length == 1) {
                final Number possiblyLen = Types.toNumber(args[0]);
                if ((possiblyLen instanceof Double) && ((Double) possiblyLen).isNaN()) {
                    arraySelf.setLength(1);
                    arraySelf.setElement(0, args[0]);
                } else {
                    arraySelf.setLength(possiblyLen.intValue());
                }
            } else {
                arraySelf.setLength(args.length);
                for (int i = 0; i < args.length; ++i) {
                    arraySelf.setElement(i, args[i]);
                }
            }
            return null;
        }
        return null;
    }

    // 15.4.2.2
    /*
     * private Object arrayWithLength(ExecutionContext context, Object arg) {
     * final Object len = arg;
     * final Number number = Types.toNumber(len);
     * final Integer integer = Types.toUint32(len);
     * if (number.intValue() != integer.intValue()) {
     * throw new RangeError();
     * }
     * final DynArray array = new DynArray(integer);
     * final PropertyDescriptor descriptor =
     * PropertyDescriptor.newDataPropertyDescriptor(true);
     * descriptor.setValue(integer);
     * array.defineOwnProperty(context, "length", descriptor, false);
     * return array;
     * }
     */

    @Override
    public JSObject createNewObject() {
        DynArray o = new DynArray();
        o.setPrototype(getPrototype());
        return o;
    }

    // ----------------------------------------------------------------------

    public static DynArray newArray(ExecutionContext context) {
        BuiltinArray ctor = (BuiltinArray) context.getGlobalObject().get(context, "Array");
        return (DynArray) context.construct(ctor);
    }
}
