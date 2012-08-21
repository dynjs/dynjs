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

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject);

        final PropertyDescriptor positiveInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        positiveInfinity.setValue(POSITIVE_INFINITY);
        this.defineOwnProperty(null, "POSITIVE_INFINITY", positiveInfinity, true);

        final PropertyDescriptor negativeInfinity = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        negativeInfinity.setValue(NEGATIVE_INFINITY);
        this.defineOwnProperty(null, "NEGATIVE_INFINITY", negativeInfinity, true);
        
        // 15.7.4
        PrimitiveDynObject prototype = new PrimitiveDynObject();
        prototype.setClassName("Number");
        prototype.setPrimitiveValue(0);
        // 15.7.4.1
        prototype.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", BuiltinNumber.this);
            }
        }, false);
        // 15.7.4.2
        prototype.defineOwnProperty(null, "toString", new PropertyDescriptor() {
            {
                set("Value", new AbstractNativeFunction(globalObject) {
                    @Override
                    public Object call(ExecutionContext context, Object self, Object... args) {
                        if (self instanceof PrimitiveDynObject) {
                            return ((PrimitiveDynObject)self).getPrimitiveValue().toString();
                        }
                        return "0";
                    }
                    
                });
            }
        }, false);
        // 15.7.4.3
        prototype.defineOwnProperty(null, "toLocaleString", new PropertyDescriptor() {            
            {
                set("Value", new AbstractNativeFunction(globalObject) {
                    @Override
                    public Object call(ExecutionContext context, Object self, Object... args) {
                        if (self instanceof PrimitiveDynObject) {
                            return ((PrimitiveDynObject)self).getPrimitiveValue().toString();
                        }
                        return "0";
                    }
                });
            }
        }, false);
        // 15.7.4.4
        prototype.defineOwnProperty(null, "valueOf", new PropertyDescriptor() {
            {
                set("Value", new AbstractNativeFunction(globalObject) {
                    @Override
                    public Object call(ExecutionContext context, Object self, Object... args) {
                        if (BuiltinNumber.isNumber((DynObject) self)) {
                            return ((PrimitiveDynObject)self).getPrimitiveValue();
                        }
                        return "TypeError";
                    }
                });
            }
        }, false);
        
        setPrototype(prototype);
    }
    
    public static boolean isNumber(DynObject object) {
        return "Number".equals(object.getClassName());
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