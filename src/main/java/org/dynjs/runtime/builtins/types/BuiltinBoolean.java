package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.bool.DynBoolean;
import org.dynjs.runtime.builtins.types.bool.prototype.ToString;
import org.dynjs.runtime.builtins.types.bool.prototype.ValueOf;

public class BuiltinBoolean extends AbstractBuiltinType {

    public BuiltinBoolean(final GlobalContext globalContext) {
        super(globalContext, "value");

        // 15.6.4 Set the prototype
        final PrimitiveDynObject proto = new DynBoolean(globalContext, Boolean.FALSE);
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setPrototype(globalContext.getPrototypeFor("Object"));
        defineNonEnumerableProperty(proto, "constructor", this );
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalContext) );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self == Types.UNDEFINED || self == Types.NULL ) {
            // 15.6.1
            return Types.toBoolean(args[0]);
        } else {
            // 15.6.2
            PrimitiveDynObject booleanObj = (PrimitiveDynObject) self;
            booleanObj.setPrimitiveValue(Types.toBoolean(args[0]));
            return booleanObj;
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynBoolean(context.getGlobalContext());
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinBoolean.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Boolean>";
    }

}
