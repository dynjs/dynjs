package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ObjectLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testEmptyObjectCreation() {
        eval("var x = {};");
        Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getValue(getContext())).isInstanceOf(JSObject.class);
    }

    @Test
    public void testNonEmptyObjectCreation() {
        eval("var x = { foo: 'bar' }; var y = x.foo;");
        Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        JSObject val = (JSObject) x.getValue(getContext());

        Object foo = val.get(getContext(), "foo");
        assertThat(foo).isEqualTo("bar");

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo("bar");
    }

    @Test
    public void testPropertyGetSet() {
        Object result = eval("var x = {",
                " taco: 'fish',",
                " get foo(){ return this.internal_foo + 'more'; },",
                " set foo(x){ this.internal_foo = x + 'cheddar'; },",
                "};",
                "x.foo = 'cheese';",
                "x.foo");
        
        assertThat( result ).isEqualTo("cheesecheddarmore");
    }
    
    @Test
    public void testPrototype() {
        Object result = eval( "var x = {};",
                "Object.getPrototypeOf(x)" );
        
        assertThat( result ).isNotSameAs( Types.UNDEFINED );
        assertThat( eval( "Object.getPrototypeOf(x)" ) ).isSameAs( eval( "Object.prototype" ) );
    }

}
