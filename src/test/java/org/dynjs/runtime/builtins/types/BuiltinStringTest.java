package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.PrimitiveDynObject;
import org.junit.Ignore;
import org.junit.Test;

public class BuiltinStringTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testConstructorWithString() {
        PrimitiveDynObject o = (PrimitiveDynObject) eval("new String('howdy')");
        assertThat(o.getClassName()).isEqualTo("String");
        assertThat(o.getPrimitiveValue()).isEqualTo("howdy");
    }

    @Test
    public void testConstructorWithNonString() {
        PrimitiveDynObject o = (PrimitiveDynObject) eval("new String(42)");
        assertThat(o.getClassName()).isEqualTo("String");
        assertThat(o.getPrimitiveValue()).isEqualTo("42");
    }
    
    @Test
    public void testValueOf() {
        Object o = eval( "new String(42).valueOf()" );
        assertThat( o ).isEqualTo( "42" );
    }
    
    @Test
    public void testValueOfOnLiteral() {
        Object o = eval( "'taco'.valueOf()" );
        assertThat( o ).isEqualTo( "taco" );
    }
    
    @Test
    public void testToString() {
        Object o = eval( "new String(42).toString()" );
        assertThat( o ).isEqualTo( "42" );
    }
    
    
    @Ignore
    @Test
    public void testCharAtWithinBounds() {
        Object o = eval( "new String(42).charAt(1)" );
        assertThat( o ).isEqualTo( "2" );
    }

}
