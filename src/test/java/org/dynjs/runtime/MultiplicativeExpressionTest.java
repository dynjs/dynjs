package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class MultiplicativeExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testDotAccess() {
        eval("var x = { foo: 'bar' }");
        assertThat(eval("x.foo")).isEqualTo("bar");
    }

    @Test
    public void testBracketAccess() {
        eval("var x = { foo: 'bar' }");
        assertThat(eval("x['foo']")).isEqualTo("bar");
    }

    @Test
    public void testBracketAccessUsingVariable() {
        eval("var x = { foo: 'bar' }",
                "var name='foo';");
        assertThat(eval("x[name]")).isEqualTo("bar");
    }
    
    @Test
    public void testModuloAroundNull() {
        Object result = eval( "var x = null; x %= null;");
    }
}