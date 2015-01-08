package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetUTCDate extends AbstractDateFunction {

    public SetUTCDate(GlobalContext globalContext) {
        super(globalContext, "date" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "setUTCDate(...) may only be used with Dates" ) );
        }
        
        DynDate dateObj = (DynDate) self;
        
        long t = dateObj.getTimeValue();
        
        Number dt = Types.toNumber(context, args[0]);
        
        Number newDate = makeDate(context, makeDay(context, yearFromTime(t), monthFromTime(t), dt), timeWithinDay(t));
        
        Number u = timeClip(context, newDate);
        
        dateObj.setTimeValue(u);
        
        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetUTCDate.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setUTCDate>";
    }
}
