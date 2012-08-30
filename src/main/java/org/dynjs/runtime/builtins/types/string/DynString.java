package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynString extends PrimitiveDynObject {
    
    public DynString(GlobalObject globalObject) {
        this( globalObject, null );
    }
    public DynString(GlobalObject globalObject, String value) {
        super( globalObject, value );
        setClassName( "String" );
        setPrototype(globalObject.getPrototypeFor("String"));
    }

}
