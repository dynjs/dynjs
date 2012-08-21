package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class BuiltinNumber extends AbstractNativeFunction {

    public static final Number POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final Number NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;

    public BuiltinNumber(GlobalObject globalObject) {
        super(globalObject);

        final PropertyDescriptor positiveInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        positiveInfinity.setValue(POSITIVE_INFINITY);
        this.defineOwnProperty(null, "POSITIVE_INFINITY", positiveInfinity, true);

        final PropertyDescriptor negativeInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        negativeInfinity.setValue(NEGATIVE_INFINITY);
        this.defineOwnProperty(null, "NEGATIVE_INFINITY", negativeInfinity, true);
        
        // 15.7.4
        DynObject prototype = new DynObject();
        prototype.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", BuiltinNumber.this);
            }
        }, false);
        prototype.defineOwnProperty(null, "toString", new PropertyDescriptor() {
            {
                set("Value", "someday it'll be implemented");
            }
        }, false);
        
        setPrototype(prototype);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self.equals(Types.UNDEFINED)) {
            // called as a function
            return Types.toNumber(args[0]);
        } else {
            // called as a ctor
            PrimitiveDynObject numberObject = (PrimitiveDynObject) self;
            numberObject.setPrimitiveValue(Types.toNumber(args[0]));
            return numberObject;
        }
    }
    
    @Override
    public JSObject createNewObject() {
        // 15.7.2.1
        PrimitiveDynObject object = new PrimitiveDynObject();
        object.setPrototype(this.getPrototype());
        object.setClassName("Number");
        return object;
    }

}