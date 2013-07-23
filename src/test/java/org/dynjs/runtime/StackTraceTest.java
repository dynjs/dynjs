package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.junit.Ignore;
import org.junit.Test;

public class StackTraceTest extends AbstractDynJSTestSupport {
    @Test
    @Ignore
    public void testBasicConstructedError() {
        DynObject e = (DynObject) eval("function foo() { return new Error(); }; foo();");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("Error\n");
    }

    @Test
    public void testBasicConstructedTypeError() {
        DynObject e = (DynObject) eval("function foo() { return new TypeError(); }; foo()");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("TypeError\n");
        assertThat(stack).contains("at foo (<eval>:1)\n");
    }

    @Test
    public void testBasicThrownError() {
        DynObject e = (DynObject) eval("function foo() { try { throw new Error(); } catch(e) { return e; } }; foo();");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("Error\n");
        assertThat(stack).contains("at foo (<eval>:1)\n");
    }

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
                    "  (function(){y.two();})();",
                    "} catch(e) {",
                    "  print(e.stack);",
                    "  throw e;",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack");
            assertThat(stack.contains("TypeError\n")).isTrue();
            assertThat(stack.contains("at foo (<eval>:5)")).isTrue();
            assertThat(stack.contains("at bar (<eval>:8)")).isTrue();
            assertThat(stack.contains("at Object.one (<eval>:2)")).isTrue();
            assertThat(stack.contains("at <anonymous> (<eval>:12)")).isTrue();
            assertThat(stack.contains("at <eval> (<eval>:12)")).isTrue();
        }
    }

    @Test
    public void testComplexStackWithMessage() {
        try {
            eval("",
                    "var x = { one: function() { bar() } };",
                    "var y = { two: x.one };",
                    "function foo() {",
                    " throw TypeError('dangit');",
                    "}",
                    "function bar() {",
                    "  foo();",
                    "}",
                    "",
                    "try {",
                    "  y.two();",
                    "} catch(e) {",
                    "  //print(e.stack);",
                    "  throw e;",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack");
            assertThat(stack.contains("TypeError: dangit")).isTrue();
            assertThat(stack.contains("at foo (<eval>:5)")).isTrue();
            assertThat(stack.contains("at bar (<eval>:8)")).isTrue();
            assertThat(stack.contains("at Object.one (<eval>:2)")).isTrue();
            assertThat(stack.contains("at <eval> (<eval>:12)")).isTrue();
        }
    }

    @Test
    // FIXME
    public void testComplexStackAndPretendWeHaveFilename() {
        /*
        try {
            getRuntime().execute("\n" +
                    "var x = { one: function() { bar() } };\n" +
                    "var y = { two: x.one };\n" +
                    "function foo() {\n" +
                    " throw TypeError();\n" +
                    "}\n" +
                    "function bar() {\n" +
                    "  foo();\n" +
                    "}\n" +
                    "\n" +
                    "try {\n" +
                    "  y.two();\n" +
                    "} catch(e) {\n" +
                    "  //print(e.stack);\n" +
                    "  throw e;\n" +
                    "}", "fakefile.js", 0);
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack");
            assertThat(stack.contains("TypeError\n")).isTrue();
            assertThat(stack.contains("at foo (fakefile.js:5)")).isTrue();
            assertThat(stack.contains("at bar (fakefile.js:8)")).isTrue();
            assertThat(stack.contains("at Object.one (fakefile.js:2)")).isTrue();
            assertThat(stack.contains("at <eval> (fakefile.js:12)")).isTrue();
        }
        */
    }
}
