package org.dynjs.runtime.builtins.types.date;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynDate extends PrimitiveDynObject {

    public DynDate(GlobalObject globalObject) {
        super(globalObject);
        setClassName("Date");
        setPrototype(globalObject.getPrototypeFor("Date"));
        setPrimitiveValue( globalObject.getRuntime().getConfig().getClock().currentTimeMillis() );
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
    
}
