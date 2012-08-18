package org.dynjs.runtime.builtins.types;

import org.dynjs.exception.RangeError;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class BuiltinArray extends AbstractNativeFunction {

    public BuiltinArray(GlobalObject globalObject) {
        super(globalObject, "meh");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (args.length == 1) {
            return arrayWithLength(context, args[0]);
        }
        return null;
    }

    // 15.4.2.2
    private Object arrayWithLength(ExecutionContext context, Object arg) {
        final Object len = arg;
        final Number number = Types.toNumber(len);
        final Integer integer = Types.toUint32(len);
        if (number.intValue() != integer.intValue()) {
            throw new RangeError();
        }
        final DynArray array = new DynArray(integer);
        final PropertyDescriptor descriptor = PropertyDescriptor.newDataPropertyDescriptor(true);
        descriptor.setValue(integer);
        array.defineOwnProperty(context, "length", descriptor, false);
        return array;
    }
}
