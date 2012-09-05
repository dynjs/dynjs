package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.junit.Test;

public class BuiltinTypeErrorTest extends AbstractDynJSTestSupport {

    @Test
    public void testConstructorWithMessage() {
        JSObject result = (JSObject) eval("new TypeError('fish') ");
        assertThat(result.get(getContext(), "name")).isEqualTo("TypeError");
        assertThat(result.get(getContext(), "message")).isEqualTo("fish");
        assertThat(result.getClassName()).isEqualTo("Error");
    }

    @Test
    public void testFunctionWithMessage() {
        JSObject result = (JSObject) eval("TypeError('fish') ");
        assertThat(result.get(getContext(), "name")).isEqualTo("TypeError");
        assertThat(result.get(getContext(), "message")).isEqualTo("fish");
        assertThat(result.getClassName()).isEqualTo("Error");
    }

    @Test
    public void testConstructorWithoutMessage() {
        JSObject result = (JSObject) eval("new TypeError() ");
        assertThat(result.get(getContext(), "name")).isEqualTo("TypeError");
        assertThat(result.get(getContext(), "message")).isEqualTo("");
        assertThat(result.getClassName()).isEqualTo("Error");
    }

    @Test
    public void testFunctionWithoutMessage() {
        JSObject result = (JSObject) eval("TypeError() ");
        assertThat(result.get(getContext(), "name")).isEqualTo("TypeError");
        assertThat(result.get(getContext(), "message")).isEqualTo("");
        assertThat(result.getClassName()).isEqualTo("Error");
    }

    @Test
    public void testToStringNoMessage() {
        assertThat(eval("TypeError().toString()")).isEqualTo("TypeError");
    }

    @Test
    public void testToStringWithMessage() {
        assertThat(eval("TypeError('that is wrong').toString()")).isEqualTo("TypeError: that is wrong");
    }
}