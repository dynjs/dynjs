package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class StackTraceTest extends AbstractDynJSTestSupport {

    @Test
    public void testComplexStack() {
        try {
            eval("",
                    "var x = { one: function() { bar() } };",
                    "var y = { two: x.one };",
                    "function foo() {",
                    " throw TypeError();",
                    "}",
                    "function bar() {",
                    "  foo();",
                    "}",
                    "",
                    "try {",
                    "  y.two();",
                    "} catch(e) {",
                    "  print(e.stack);",
                    "  throw e;",
                    "}");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack" );
            System.err.println( stack );
            assertThat( stack.contains( "TypeError" ) ).isTrue();
            assertThat( stack.contains( "at foo (<eval>:5)" ) ).isTrue();
            assertThat( stack.contains( "at bar (<eval>:8)" ) ).isTrue();
            assertThat( stack.contains( "at Object.one (<eval>:2)" ) ).isTrue();
            assertThat( stack.contains( "at <eval> (<eval>:12)" ) ).isTrue();
        }
    }
}