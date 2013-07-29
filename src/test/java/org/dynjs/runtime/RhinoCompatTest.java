package org.dynjs.runtime;

import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test Rhino compatibility
 */
public class RhinoCompatTest extends AbstractDynJSTestSupport {
    @Test
    public void testDefineGetter() {
        eval("f = function() {}");
        eval("x = {}");
        assertThat(eval("typeof Object.prototype.__defineGetter__")).isEqualTo("function");
        assertThat(eval("typeof x.__defineGetter__")).isEqualTo("function");
        assertThat(eval("typeof f.__defineGetter__")).isEqualTo("function");

        eval("x.__defineGetter__('foo', function() { return 'bar' });");
        assertThat(eval("x.foo")).isEqualTo("bar");
    }

    @Test
    public void testDefineSetter() {
        assertThat(eval("typeof Object.prototype.__defineSetter__")).isEqualTo("function");
        eval("x = {};");
        eval("x.__defineSetter__('foo', function(val) { bar = val; });");
        eval("x.__defineGetter__('foo', function() { return 'foobar'; });");
        assertThat(eval("typeof x.__defineSetter__")).isEqualTo("function");
        assertThat(eval("x.foo")).isEqualTo("foobar");
        eval("x.foo = 'bar'");
        assertThat(eval("x.foo")).isEqualTo("foobar");
    }

    @Test
    public void testDefineSetterInCtor() {
        assertThat(eval("typeof Object.prototype.__defineSetter__")).isEqualTo("function");
        eval(
                "var X = function() {" +
                        "this.bar = 'bar';" +
                        "this.__defineSetter__('foo', function(val) { this.bar = val; });" +
                        "this.__defineGetter__('foo', function() { return this.bar; });" +
                        "};" +
                        "x = new X();"
        );
        assertThat(eval("typeof x.__defineSetter__")).isEqualTo("function");
        assertThat(eval("x.foo")).isEqualTo("bar");
        eval("x.foo = 'foobar'");
        assertThat(eval("x.foo")).isEqualTo("foobar");
    }

    @Test
    public void testLookupGetter() {
        assertThat(eval("var x = {}; x.__lookupGetter__()")).isEqualTo(Types.UNDEFINED);
        assertThat(eval("x.__lookupGetter__('foo')")).isEqualTo(Types.UNDEFINED);
        eval("x.__defineGetter__('foo', function() { return bar; });");
        assertThat(eval("x.__lookupGetter__('foo')")).isInstanceOf(JSFunction.class);
    }

    @Test
    public void testLookupSetter() {
        assertThat(eval("typeof Object.prototype.__defineSetter__")).isEqualTo("function");
        eval("x = {};");
        assertThat(eval("x.__lookupSetter__()")).isEqualTo(Types.UNDEFINED);
        eval("x.__defineSetter__('foo', function(val) { bar = val; });");
        assertThat(eval("x.__lookupSetter__")).isInstanceOf(JSFunction.class);
    }

}
