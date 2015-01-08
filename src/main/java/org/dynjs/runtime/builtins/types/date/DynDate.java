package org.dynjs.runtime.builtins.types.date;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;

public class DynDate extends PrimitiveDynObject {

    public DynDate(GlobalContext globalContext) {
        super(globalContext);
        setClassName("Date");
        setPrototype(globalContext.getPrototypeFor("Date"));
        setPrimitiveValue( globalContext.getRuntime().getConfig().getClock().currentTimeMillis() );
    }
    
    public long getTimeValue() {
        return ((Number) getPrimitiveValue()).longValue();
    }
    
    public void setTimeValue(Number timeValue) {
        setPrimitiveValue(timeValue);
    }
    
    public boolean isNaN() {
        return Double.isNaN( ((Number)getPrimitiveValue()).doubleValue() );
    }
    
    @Override
    public Object defaultValue(ExecutionContext context, String hint) {
        // 8.12.8

        if (hint == null) {
            hint = "String";
        }

        if (hint.equals("String")) {
            Object toString = get(context, "toString");
            if (toString instanceof JSFunction) {
                Object result = context.call((JSFunction) toString, this);
                if (result instanceof String || result instanceof Number || result instanceof Boolean || result == Types.UNDEFINED || result == Types.NULL) {
                    return result;
                }
            }

            Object valueOf = get(context, "valueOf");
            if (valueOf instanceof JSFunction) {
                Object result = context.call((JSFunction) valueOf, this);
                if (result instanceof String || result instanceof Number || result instanceof Boolean || result == Types.UNDEFINED || result == Types.NULL) {
                    return result;
                }
            }
            throw new ThrowException(context, context.createTypeError("String coercion must return a primitive value"));
        } else if (hint.equals("Number")) {
            Object valueOf = get(context, "valueOf");
            if (valueOf instanceof JSFunction) {
                Object result = context.call((JSFunction) valueOf, this);
                if (result instanceof String || result instanceof Number || result instanceof Boolean || result == Types.UNDEFINED || result == Types.NULL) {
                    return result;
                }
            }

            Object toString = get(context, "toString");
            if (toString instanceof JSFunction) {
                Object result = context.call((JSFunction) toString, this);
                if (result instanceof String || result instanceof Number || result instanceof Boolean || result == Types.UNDEFINED || result == Types.NULL) {
                    return result;
                }
            }
            throw new ThrowException(context, context.createTypeError("String coercion must return a primitive value"));
        }

        return null;
    }
    
}
