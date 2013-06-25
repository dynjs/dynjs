package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class GetUTCDay extends AbstractDateFunction {

    public GetUTCDay(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "getUTCDay() may only be used with Dates" ) );
        }
        
        DynDate date = (DynDate) self;
        
        if ( date.isNaN() ) {
            return Double.NaN;
        }
        
        long t = date.getTimeValue();
        return (long) weekday( t );
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/GetUTCDay.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: getUTCDay>";
    }
}
