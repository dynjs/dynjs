package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class GetUTCSeconds extends AbstractDateFunction {

    public GetUTCSeconds(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "getUTCSeconds() may only be used with Dates" ) );
        }
        
        DynDate date = (DynDate) self;
        
        if ( date.isNaN() ) {
            return Double.NaN;
        }
        
        long t = date.getTimeValue();
        return (long) secFromTime(t);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/GetUTCSeconds.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: getUTCSeconds>";
    }
}
