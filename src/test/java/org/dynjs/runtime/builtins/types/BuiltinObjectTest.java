package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.junit.Test;

public class BuiltinObjectTest extends AbstractDynJSTestSupport {

    @Test
    public void testConstructorWithObject() {
        eval("var x = {}; var y = new Object(x);");

        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());

        assertThat(y).isSameAs(x);
    }

    @Test
    public void testConstructorWithString() {
        eval("var x = new Object( 'howdy' );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo("howdy");
    }

    @Test
    public void testConstructorWithBoolean() {
        eval("var x = new Object( true );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo(true);
    }

    @Test
    public void testConstructorWithInteger() {
        eval("var x = new Object( 42 );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo(42);
    }

    @Test
    public void testConstructWithoutArg() {
        eval("var x = new Object();");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(DynObject.class);
    }

    @Test
    public void testFreeze() {
        Object result = eval("var x = { foo: 'taco' };",
                "x.foo = 'cheese';",
                "x.foo;");

        assertThat( result ).isEqualTo( "cheese" );

        result = eval("var x = { foo: 'taco' };",
                "Object.freeze(x);",
                "x.foo = 'cheese';",
                "x.foo;");
        
        assertThat( result ).isEqualTo( "taco" );

    }
}
