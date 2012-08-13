package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Types;

public class ParseFloat extends AbstractNativeFunction {
    
    public ParseFloat(LexicalEnvironment scope, boolean strict) {
        super( scope, strict, "f" );
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object f = args[0];
        if ( f != Types.UNDEFINED ) {
            try {
                return Double.parseDouble( f.toString() );
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return Types.UNDEFINED;
    }

}
