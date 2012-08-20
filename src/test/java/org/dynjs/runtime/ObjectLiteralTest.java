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
        eval( "var x = { foo: 'bar' }; var y = x.foo;" );
        Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        JSObject val = (JSObject) x.getValue(getContext());
        
        Object foo = val.get( getContext(), "foo" );
        assertThat( foo ).isEqualTo( "bar" );
        
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat( y ).isEqualTo( "bar" );
    }

}
