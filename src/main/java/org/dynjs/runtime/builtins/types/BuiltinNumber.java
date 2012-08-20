package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class BuiltinNumber extends AbstractNativeFunction {

    public static final Number POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final Number NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    
    public BuiltinNumber(GlobalObject globalObject) {
        super(globalObject, "value");
        defineDefaultsAccessorProperty(this, "POSITIVE_INFINITY", POSITIVE_INFINITY);
        defineDefaultsAccessorProperty(this, "NEGATIVE_INFINITY", NEGATIVE_INFINITY);
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self.equals(Types.UNDEFINED)) {
            // called as a function
            return Types.toNumber(args[0]);
        } else {
            // called as a ctor
            // TODO: we never get here
            return "Not implemented";
        }
    }
    
    @Override
    public JSObject getPrototype( GlobalObject globalObject ) {
        if (null == PROTOTYPE) {
            PROTOTYPE = new AbstractNativeFunction(globalObject) {
                {
                    defineDefaultsAccessorProperty(this, "POSITIVE_INFINITY", POSITIVE_INFINITY);
                    defineDefaultsAccessorProperty(this, "NEGATIVE_INFINITY", NEGATIVE_INFINITY);
                    this.setClassName("Number");
                }
                @Override
                public Object call(ExecutionContext context, Object self, Object... args) {
                    System.err.println("Calling Number prototype.");
                    return null;
                }
                @Override
                public JSObject getPrototype(GlobalObject globalObject) {
                    System.err.println("Calling Number prototype.prototype");
                    return null;
                }
            };
        }
        return PROTOTYPE;
    }
    
    protected static void defineDefaultsAccessorProperty(JSObject object, String name, Object value) {
        final PropertyDescriptor property = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        property.setValue(value);
        object.defineOwnProperty(null, name, property, true);
   }
    
    private static JSObject PROTOTYPE;
    
}