package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

public class ParseFloat extends AbstractNativeFunction {
    
    public ParseFloat(LexicalEnvironment scope, boolean strict) {
        super( scope, strict, "f" );
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Reference f = context.resolve( "f" );
        Object v = f.getValue( context );
        if ( v != Types.UNDEFINED ) {
            try {
                return Double.parseDouble( v.toString() );
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return Types.UNDEFINED;
    }

}
