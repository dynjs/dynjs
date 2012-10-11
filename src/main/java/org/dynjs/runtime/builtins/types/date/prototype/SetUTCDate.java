package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetUTCDate extends AbstractDateFunction {

    public SetUTCDate(GlobalObject globalObject) {
        super(globalObject, "date" );
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
}
