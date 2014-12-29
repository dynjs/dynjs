package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class JSONTest extends AbstractDynJSTestSupport {

    @Test
    public void testParseArray() {
        DynArray result = (DynArray) eval("JSON.parse('[42,86]')");
        assertThat(result.get(getContext(), "length")).isEqualTo(2L);
        assertThat(result.get(getContext(), "0")).isEqualTo(42);
        assertThat(result.get(getContext(), "1")).isEqualTo(86);
    }

    @Test
    public void testParseNestedArray() {
        DynArray result = (DynArray) eval("JSON.parse('[42,86,[\"a\", \"b\", \"c\" ]]')");
        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo(42);
        assertThat(result.get(getContext(), "1")).isEqualTo(86);
        DynArray nested = (DynArray) result.get(getContext(), "2");
        assertThat(nested.get(getContext(), "length")).isEqualTo(3L);
        assertThat(nested.get(getContext(), "0")).isEqualTo("a");
        assertThat(nested.get(getContext(), "1")).isEqualTo("b");
        assertThat(nested.get(getContext(), "2")).isEqualTo("c");
    }

    @Test
    public void testParseObject() {
        JSObject result = (JSObject) eval("JSON.parse('{\"foo\": 42, \"bar\": \"cheese\"}')");

        assertThat(result.get(getContext(), "foo")).isEqualTo(42);
        assertThat(result.get(getContext(), "bar")).isEqualTo("cheese");
    }

    @Test
    public void testParseWithReviver() {
        JSObject result = (JSObject) eval("JSON.parse( '[1,2,3,4,5,6,7]', function(name,val){",
                "if ( name != '' ) {",
                "  if ( val % 2 == 0 ) {",
                "    return undefined;",
                "  } else {",
                "    return val;",
                "  }",
                "} else {",
                "  return val;",
                "}",
                "})");

        assertThat(result.get(getContext(), "length")).isEqualTo(7L);
        assertThat(result.get(getContext(), "0")).isEqualTo(1);
        assertThat(result.get(getContext(), "1")).isEqualTo(Types.UNDEFINED);
        assertThat(result.get(getContext(), "2")).isEqualTo(3);
        assertThat(result.get(getContext(), "3")).isEqualTo(Types.UNDEFINED);
        assertThat(result.get(getContext(), "4")).isEqualTo(5);
        assertThat(result.get(getContext(), "5")).isEqualTo(Types.UNDEFINED);
        assertThat(result.get(getContext(), "6")).isEqualTo(7);
    }

    @Test
    public void testParseLongValue() {
        JSObject result = (JSObject) eval("JSON.parse('{\"foo\": 1234567890123, \"bar\": \"cheese\"}')");

        assertThat(result.get(getContext(), "foo")).isEqualTo(1234567890123L);
        assertThat(result.get(getContext(), "bar")).isEqualTo("cheese");
    }

    @Test
    public void testStringifyArray() {
        String result = (String) eval("JSON.stringify( [1, 'foo', 3] )");
        assertThat(result).isEqualTo("[1,\"foo\",3]");
    }

    @Test
    public void testStringifyArrayWithIndent() {
        String result = (String) eval("JSON.stringify( [1, 'foo', 3], undefined, '  ' )");
        assertThat(result).isEqualTo("[\n" +
                "  1,\n" +
                "  \"foo\",\n" +
                "  3\n" +
                "]");

    }

    @Test
    public void testStringifyObject() {
        eval("var x = {",
                "foo: 42,",
                "fish: 'taco',",
                "}");

        String result = (String) eval("JSON.stringify(x)");
        assertThat(result).contains("\"fish\":\"taco\"");
        assertThat(result).contains("\"foo\":42");
        assertThat(result).startsWith("{");
        assertThat(result).endsWith("}");
        assertThat(result).doesNotContain(" ");
        assertThat(result).doesNotContain("\n");
    }

    @Test
    public void testStringifyObjectWithPropertyList() {
        eval("var x = {",
                "foo: 42,",
                "fish: 'taco',",
                "}");

        String result = (String) eval("JSON.stringify(x, ['foo'])");
        assertThat(result).doesNotContain("\"fish\":\"taco\"");
        assertThat(result).contains("\"foo\":42");
        assertThat(result).startsWith("{");
        assertThat(result).endsWith("}");
        assertThat(result).doesNotContain(" ");
        assertThat(result).doesNotContain("\n");
    }

    @Test
    public void testStringifyDate() {
        String result = (String) eval("JSON.stringify(new Date(Date.UTC(1980, 2, 11)))");
        assertThat(result).isEqualTo("\"1980-03-11T00:00:00.000Z\"");
    }

}
