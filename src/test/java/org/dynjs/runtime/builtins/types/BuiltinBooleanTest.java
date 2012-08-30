package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.builtins.types.bool.DynBoolean;
import org.junit.Test;

public class BuiltinBooleanTest extends AbstractDynJSTestSupport {

    @Test
    public void testBooleanFunction() {
        assertThat( eval( "Boolean('true')" )).isEqualTo( true );
        assertThat( eval( "Boolean('anything')" )).isEqualTo( true );
        assertThat( eval( "Boolean('')" )).isEqualTo( false );
    }
    
    @Test
    public void testBooleanConstructor() {
        JSObject result = (JSObject) eval( "new Boolean(true)" );
        assertThat( result ).isInstanceOf(DynBoolean.class);
        assertThat( ((DynBoolean)result).getPrimitiveValue() ).isEqualTo(true);
    }
    
    
    @Test
    public void testBooleanConstructorWithString() {
        JSObject result = (JSObject) eval( "new Boolean('')" );
        assertThat( result ).isInstanceOf(DynBoolean.class);
        assertThat( ((DynBoolean)result).getPrimitiveValue() ).isEqualTo(false);
    }
    
    @Test
    public void testPrototype() {
        assertThat( eval( "Object.getPrototypeOf( new Boolean() )" ) ).isSameAs( eval("Boolean.prototype" ));
    }
    
    @Test
    public void testToString() {
        assertThat( eval( "new Boolean(true).toString()" ) ).isEqualTo( "true" );
        assertThat( eval( "new Boolean(false).toString()" ) ).isEqualTo( "false" );
    }
    
    @Test
    public void testValueOf() {
        assertThat( eval( "new Boolean(true).valueOf()" ) ).isEqualTo( true );
        assertThat( eval( "new Boolean(false).valueOf()" ) ).isEqualTo( false );
    }
}