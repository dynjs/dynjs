package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynString extends PrimitiveDynObject {
    
    public DynString(ExecutionContext context) {
        this( context, null );
    }
    public DynString(ExecutionContext context, String value) {
        super( value );
        setClassName( "String" );
        setPrototype(context.getPrototypeFor("String"));
    }

}
