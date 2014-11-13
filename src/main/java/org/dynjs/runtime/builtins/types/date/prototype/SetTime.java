package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetTime extends AbstractDateFunction {

    public SetTime(GlobalContext globalContext) {
        super(globalContext, "time" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( ! ( self instanceof DynDate ) ) {
            throw new ThrowException( context, context.createTypeError( "setTime(...) may only be used with Dates" ) );
        }
        
        DynDate dateObj = (DynDate) self;
        
        Number v = timeClip(context, Types.toNumber(context, args[0]));
        dateObj.setTimeValue(v);
        return v;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetTime.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setTime>";
    }
}
