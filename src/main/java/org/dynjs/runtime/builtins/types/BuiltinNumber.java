package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.PositiveInfinity;
import org.dynjs.runtime.builtins.types.number.ToFixed;
import org.dynjs.runtime.builtins.types.number.NaN;
import org.dynjs.runtime.builtins.types.number.ToLocaleString;
import org.dynjs.runtime.builtins.types.number.ToString;
import org.dynjs.runtime.builtins.types.number.ValueOf;

public class BuiltinNumber extends AbstractNativeFunction {

    public static final Number POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final Number NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final Number NaN = Double.NaN;

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, true );

        // 15.7.4 Set the prototype
        PrimitiveDynObject prototype = new PrimitiveDynObject();
        prototype.setClassName("Number");
        prototype.setPrimitiveValue(0);
        defineProperty(prototype, "constructor",     BuiltinNumber.this);                // 15.7.4.1
        defineProperty(prototype, "toString",        new ToString(globalObject));        // 15.7.4.2
        defineProperty(prototype, "toLocaleString",  new ToLocaleString(globalObject));  // 15.7.4.3
        defineProperty(prototype, "valueOf",         new ValueOf(globalObject));         // 15.7.4.4
        defineProperty(prototype, "toFixed",         new ToFixed(globalObject));         // 15.7.4.5        
        setPrototype(prototype);

        defineAccessorProperty(this, "NaN", new NaN(globalObject));
        globalObject.defineGlobalProperty("NaN", new NaN(globalObject));
        defineAccessorProperty(this, "POSITIVE_INFINITY", new PositiveInfinity(globalObject));
        defineAccessorProperty(this, "NEGATIVE_INFINITY", NEGATIVE_INFINITY);
    }
    
    private void defineProperty(DynObject object, String name, final Object value) {
        object.defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set("Value", value);
            }
        }, false);

    }
    
    private void defineAccessorProperty(DynObject object, String name, Object value) {
        PropertyDescriptor descriptor = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        descriptor.setValue(value);
        object.defineOwnProperty(null, name, descriptor, false);
    }
    
    public static boolean isNumber(DynObject object) {
        return "Number".equals(object.getClassName());
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        System.err.println( "NUMBER SELF: " + self );
        if (self == Types.UNDEFINED ) {
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