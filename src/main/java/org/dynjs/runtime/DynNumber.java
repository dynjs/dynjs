package org.dynjs.runtime;

public class DynNumber extends AbstractNativeFunction {

    public static final Number POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final Number NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;

    public DynNumber(GlobalObject globalObject) {
        super(globalObject, "value");
        final PropertyDescriptor positiveInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        positiveInfinity.setValue(POSITIVE_INFINITY);
        this.defineOwnProperty(null, "POSITIVE_INFINITY", positiveInfinity, true);
        final PropertyDescriptor negativeInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        negativeInfinity.setValue(NEGATIVE_INFINITY);
        this.defineOwnProperty(null, "NEGATIVE_INFINITY", negativeInfinity, true);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self == null) {
            return Types.toNumber(args[0]);
        }
        return 10;
    }
    
}