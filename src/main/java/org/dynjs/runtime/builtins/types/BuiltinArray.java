package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.array.ToString;

public class BuiltinArray extends AbstractNativeFunction {

    public BuiltinArray(final GlobalObject globalObject) {
        super(globalObject);
        DynArray proto = new DynArray();
        proto.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", BuiltinArray.this);
            }
        }, false);
        proto.defineOwnProperty(null, "toString", new PropertyDescriptor() {
            {
                set( "Value", new ToString( globalObject ) );
            }
        }, false);

        setPrototype(proto);

        System.err.println("prototype: " + proto);
        Object ctor = getPrototype().get(null, "constructor");
        System.err.println("ctor: " + ctor + " // " + ctor.getClass());
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        DynArray arraySelf = (DynArray) self;
        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        System.err.println("argsObj: " + argsObj);
        int numArgs = (int) argsObj.get(context, "length");

        if (self != Types.UNDEFINED) {
            System.err.println("construct");
            if (numArgs == 1) {
                final Number possiblyLen = Types.toNumber(argsObj.get(context, "0"));
                if ((possiblyLen instanceof Double) && ((Double) possiblyLen).isNaN()) {
                    arraySelf.setLength(1);
                    arraySelf.setElement(0, argsObj.get(context, "" + 0));
                } else {
                    arraySelf.setLength(possiblyLen.intValue());
                }
            } else {
                arraySelf.setLength(numArgs);
                for (int i = 0; i < numArgs; ++i) {
                    arraySelf.setElement(i, argsObj.get(context, "" + i));
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
}
