package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class BuiltinError extends AbstractNativeFunction {

    public BuiltinError(final GlobalObject globalObject) {
        super(globalObject);
        DynArray proto = new DynArray();
        proto.setClassName( "Error" );
        proto.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", BuiltinError.this);
            }
        }, false);
        proto.defineOwnProperty(null, "name", new PropertyDescriptor() {
            {
                set("Value", "Error" );
            }
        }, false);
        proto.defineOwnProperty(null, "message", new PropertyDescriptor() {
            {
                set("Value", "" );
            }
        }, false);
        setPrototype(proto);
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        DynArray arraySelf = (DynArray) self;

        if (self != Types.UNDEFINED) {
            System.err.println("construct");
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
    
    public static DynArray newArrayLiteral(ExecutionContext context) {
        BuiltinError ctor = (BuiltinError) context.getGlobalObject().get(context, "Array" );
        return (DynArray) context.construct(ctor);
    }
}
