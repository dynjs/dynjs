package org.dynjs.runtime.builtins.types.bool;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynBoolean extends PrimitiveDynObject {

    public DynBoolean(GlobalObject globalObject) {
        this( globalObject, null );
    }
    
    public DynBoolean(GlobalObject globalObject, Boolean value) {
        super( globalObject, value );
        setClassName( "Boolean" );
        setPrototype(globalObject.getPrototypeFor("Boolean"));
    }
    
}