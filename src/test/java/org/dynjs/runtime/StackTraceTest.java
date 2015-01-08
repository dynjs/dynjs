package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.builtins.types.error.StackElement;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class StackTraceTest extends AbstractDynJSTestSupport {
    @Test
    public void testBasicConstructedError() {
        DynObject e = (DynObject) eval("function foo() { return new Error(); }; foo();");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("Error\n");
        assertThat(stack).contains("at foo (<eval>:1:17)\n");
    }

    @Test
    public void testBasicConstructedTypeError() {
        DynObject e = (DynObject) eval("function foo() { return new TypeError(); }; foo()");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("TypeError\n");
        assertThat(stack).contains("at foo (<eval>:1:17)\n");
    }

    @Test
    public void testBasicThrownError() {
        DynObject e = (DynObject) eval("function foo() { try { throw new Error(); } catch(e) { return e; } }; foo();");

        String stack = (String) e.get(getContext(), "stack");
        assertThat(stack).contains("Error\n");
        assertThat(stack).contains("at foo (<eval>:1:55)\n");
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
                    "  throw e;",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack");
            assertThat(stack.contains("TypeError\n")).isTrue();
            assertThat(stack.contains("at foo (<eval>:5:2)")).isTrue();
            assertThat(stack.contains("at bar (<eval>:8:3)")).isTrue();
            assertThat(stack.contains("at Object.one (<eval>:2:29)")).isTrue();
            assertThat(stack.contains("at <anonymous> (<eval>:12:15)")).isTrue();
            assertThat(stack.contains("at <eval> (<eval>:12:3)")).isTrue();
        }
    }

    @Test
    public void simpleFunctionsAssignedToVariable() {
        try {
            eval("   var x = function() {",
                    "  throw new Error('dang');",
                    "};",
                    "x();");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get( getContext(), "stack" );
            assertThat( stack.contains( "at x")).isTrue();
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
                    "  print(e.stack);",
                    "  throw e;",
                    "}");
            throw new AssertionError("Should have thrown");
        } catch (ThrowException e) {
            JSObject o = (JSObject) e.getValue();
            String stack = (String) o.get(getContext(), "stack");
            assertThat(stack.contains("TypeError: dangit")).isTrue();
            assertThat(stack.contains("at foo (<eval>:5:2)")).isTrue();
            assertThat(stack.contains("at bar (<eval>:8:3)")).isTrue();
            assertThat(stack.contains("at Object.one (<eval>:2:29)")).isTrue();
            assertThat(stack.contains("at <eval> (<eval>:12:3)")).isTrue();
        }
    }

    @Test
    public void testNativeStackTrace() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "var val = null;" +
                "try {" +
                "  require('notexist.js');" +
                "} catch(e) {" +
                "  val = e.stack[1];" +
                "}" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat(stackElement.isNative()).isTrue();
    }

    @Test
    public void testStackElementIsTopLevel() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "var e = new Error('broken china');" +
                "var val = e.stack[0];" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat(stackElement.isTopLevel()).isTrue();
    }

    @Test
    public void testStackElementIsNotTopLevel() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "var ErrorMaker = function() {};" +
                "ErrorMaker.prototype.makeIt = function() { return new Error('broken china'); };" +
                "var e = new ErrorMaker().makeIt();" +
                "var val = e.stack[0];" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat(stackElement.isTopLevel()).isFalse();
    }

    @Test
    public void testStackElementIsConstructor() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "var ErrorMaker = function() { throw new Error('broken china'); };" +
                "try {" +
                "  new ErrorMaker();" +
                "} catch(e) {" +
                "  var val = e.stack[0];" +
                "}" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat(stackElement.isConstructor()).isTrue();
    }

    @Test
    public void testStackElementIsNotConstructor() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "var e = new Error('broken china');" +
                "var val = e.stack[0];" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat(stackElement.isConstructor()).isFalse();
    }

    @Test
    public void testStackElementIsEval() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "try {" +
                "  eval('throw new Error()');" +
                "} catch(e) {" +
                "  var val = e.stack[1];" +
                "}" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat((stackElement).isEval()).isTrue();
    }

    @Test
    public void testStackElementEvalOrigin() {
        StackElement stackElement = (StackElement) eval("__prepareStackTrace = Error.prepareStackTrace;" +
                "Error.prepareStackTrace = function(e,s) { return s; };" +
                "try {" +
                "  function thrower() {" +
                "    eval('throw new Error()');" +
                "  }" +
                "  thrower();" +
                "} catch(e) {" +
                "  print(__prepareStackTrace(e,e.stack));" +
                "  var val = e.stack[1];" +
                "}" +
                "Error.prepareStackTrace = __prepareStackTrace;" +
                "val;");
        assertThat(stackElement).isNotNull();
        assertThat((stackElement).isEval()).isTrue();
        StackElement origin = stackElement.getEvalOrigin();
        assertThat(origin).isNotNull();
        assertThat(origin.getFunctionName()).isEqualTo("thrower");
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
