package org.dynjs.runtime;

import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

public class FunctionDeclarationTest extends AbstractDynJSTestSupport {

    @Test
    public void testFunctionDeclaration() {
        eval("function foo() { return 42.0 };");
        Reference foo = getContext().resolve("foo");
        assertThat(foo).isNotNull();
        assertThat(foo.isUnresolvableReference()).isFalse();
        JSFunction fn = (JSFunction) foo.getValue(getContext());
        assertThat(fn).isNotNull();

        Object result = fn.call(getContext());
        assertThat(result).isEqualTo(42.0);
    }

    @Test
    public void testInvalidFunction() {
        try {
            eval("function (){};");
            fail("Invalid functions should be invalid, dammit.");
        } catch (Exception e) {

        }
    }

    @Test
    public void testValidFunctionExpr() {
        eval("(function (){});");
    }
    
    @Test
    @Ignore
    public void testFunctionPropertyNamedPrint() {
        eval("var o = new Object();");
        eval("o.print = function() { return 'printed' }");
        assertThat(eval("o.print()")).isEqualTo("printed");
    }

}
