package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.junit.Test;

public class BuiltinFunctionTest extends AbstractDynJSTestSupport {

    @Test
    public void testConstructor() {
        Object result = eval("var f = new Function('x', 'return x*2;');",
                "f(42);");

        assertThat(result).isEqualTo(84);
    }

    @Test
    public void testFunction() {
        Object result = eval("var f = Function('x', 'return x*2;');",
                "f(42);");

        assertThat(result).isEqualTo(84);
    }

    @Test
    public void testFunctionWithGlobbedUpFormalParams() {
        Object result = eval("var f = Function('x,y', 'return x*y*2;');",
                "f(42,10);");

        assertThat(result).isEqualTo(840);
    }

    @Test
    public void testFunctionLength() {
        assertThat(eval("Function.length")).isEqualTo(1);
    }

    @Test
    public void testPrototypeConstructor() {
        assertThat(eval("Function.prototype.constructor")).isNotNull().isInstanceOf(JSFunction.class);
        assertThat(eval("Function.prototype.constructor")).isSameAs(eval("Function"));
    }

    @Test
    public void testToString() {
        Object str = eval("Function('x', 'if(false){}else{};return x*2;').toString()");
    }

    @Test
    public void testToStringNative() {
        Object str = eval("parseInt.toString()");
    }

    @Test
    public void testApply() {
        Object result = eval("var thing = { taco: 20 };",
                "var f = function(x) {",
                "  return this.taco + x;",
                "};",
                "f.apply(thing,[42]);");
        assertThat(result).isEqualTo(62);
    }

    @Test
    public void testCall() {
        Object result = eval("var thing = { taco: 20 };",
                "var f = function(x) {",
                "  return this.taco + x;",
                "};",
                "f.call(thing,42);");
        assertThat(result).isEqualTo(62);
    }

    @Test
    public void testBindFunction() {
        Object result = eval("var self = { z: 10 };",
                "var f = function(x,y) { this.z + x + y };",
                "var b = f.bind(self, 20);",
                "b(42);");

        assertThat(result).isEqualTo(72);
    }

    @Test
    public void testBindConstructorWithoutSelf() {
        JSObject result = (JSObject) eval("var ctor = function(x){ return { taco:x } };",
                "var bound_ctor = ctor.bind(undefined, 42);",
                "new bound_ctor()");

        assertThat(result.get(getContext(), "taco")).isEqualTo(42);
    }

    @Test
    public void testBindConstructorWithSelf() {
        JSObject result = (JSObject) eval("var b = 99;",
                "var ctor = function(x){ return { taco:x } };",
                "var bound_ctor = ctor.bind(b, 42);",
                "new bound_ctor()");

        assertThat(result.get(getContext(), "taco")).isEqualTo(42);
    }

    @Test(expected = ThrowException.class)
    public void testArgumentsThrowOnBindedFunction() {
        eval("var f = function(){ print(arguments); return 42; }",
                "var b = f.bind();",
                "b.arguments");
    }

    @Test(expected = ThrowException.class)
    public void testCallerThrowOnBindedFunction() {
        eval("var f = function(){ print(arguments); return 42; }",
                "var b = f.bind();",
                "b.caller");
    }

    @Test
    public void testFunctionPrototypeFunctionPrototypes_MetaMetaMeta() {
        try {
            eval("Function.prototype.toString.call()");
            throw new AssertionError("Should have throw TypeError");
        } catch (ThrowException e) {
            JSObject value = (JSObject) e.getValue();
            assertThat( (String) value.get( getContext(), "message" ) ).contains( "only allowed on functions" );
        }
    }

}
