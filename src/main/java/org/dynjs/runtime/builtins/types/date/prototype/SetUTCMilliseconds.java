package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetUTCMilliseconds extends AbstractDateFunction {

    public SetUTCMilliseconds(GlobalContext globalContext) {
        super(globalContext, "ms");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "setUTCMilliseconds(...) may only be used with Dates" ) );
        }
        
        DynDate dateObj = (DynDate) self;
        
        long t = dateObj.getTimeValue();
        
        Number time = makeTime(context, hourFromTime(t), minFromTime(t), secFromTime(t), Types.toNumber(context, args[0]));
        Number u = timeClip(context, makeDate( context, day(t), time ) );
        
        dateObj.setTimeValue(u);
        
        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetUTCMilliseconds.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setUTCMilliseconds>";
    }
}
