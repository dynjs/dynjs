package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class GetUTCMilliseconds extends AbstractDateFunction {

    public GetUTCMilliseconds(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "getUTCMilliseconds() may only be used with Dates" ) );
        }
        
        DynDate date = (DynDate) self;
        
        if ( date.isNaN() ) {
            return Double.NaN;
        }
        
        long t = date.getTimeValue();
        return msFromTime(t);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/GetUTCMilliseconds.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: getUTCMilliseconds>";
    }
}
