package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynNumber extends PrimitiveDynObject {

    public DynNumber(GlobalObject globalObject) {
        this( globalObject, null );
    }
    
    public DynNumber(GlobalObject globalObject, Number value) {
        super( value );
        setClassName( "Number" );
        setPrototype(globalObject.getPrototypeFor("Number"));
    }
    
}