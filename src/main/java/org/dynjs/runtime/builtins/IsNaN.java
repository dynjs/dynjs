package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class IsNaN extends AbstractNativeFunction {
    
    public IsNaN(GlobalObject globalObject, boolean strict) {
        super( globalObject, "o" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if ( o != Types.UNDEFINED ) {
            if ( isNullOrBooleanOrWhiteSpace( o.toString()  ) ) {
                return false;
            }
        }
        return Types.UNDEFINED;
    }
    
    private boolean isNullOrBooleanOrWhiteSpace(String value) {
        return (value.equals( "" ) || value.equals( "null" ) || value.equals( "true" ) || value.equals( "false" ));
    }

}
