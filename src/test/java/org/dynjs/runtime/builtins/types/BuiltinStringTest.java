package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.junit.Ignore;
import org.junit.Test;

public class BuiltinStringTest extends AbstractDynJSTestSupport {

    @Test
    public void testFromCharCode() {
        assertThat(eval("String.fromCharCode(66, 79, 66)")).isEqualTo("BOB");
    }

    @Test
    public void testConstructorWithString() {
        PrimitiveDynObject o = (PrimitiveDynObject) eval("new String('howdy')");
        assertThat(o.getClassName()).isEqualTo("String");
        assertThat(o.getPrimitiveValue()).isEqualTo("howdy");
    }

    @Test
    public void testConstructorWithNonString() {
        PrimitiveDynObject o = (PrimitiveDynObject) eval("new String(42)");
        assertThat(o.getClassName()).isEqualTo("String");
        assertThat(o.getPrimitiveValue()).isEqualTo("42");
    }

    @Test
    public void testConstructorWithExponentialNumber() {
        PrimitiveDynObject o = (PrimitiveDynObject) eval("new  String(.00000012345);");
        assertThat(o.getPrimitiveValue()).isEqualTo("1.2345e-7");
    }

    @Test
    public void testReplace() {
        Object o = eval("String('fat').replace('f', 'ph')");
        assertThat(o).isEqualTo("phat");
    }

    @Test
    public void testReplaceWithNestedBracket() {
        Object o = eval("\"[ [ 'a', 'b', 'c' ] ]\".replace(/^\\[ | \\]$/g, '')");
        assertThat(o).isEqualTo("[ 'a', 'b', 'c' ]");
    }

    @Test
    public void testReplaceWithMultipleNestedBracket() {
        Object o = eval("\"[ [ 'a', 'b', 'c' ], [ 'd', 'e', 'f' ] ]\".replace(/^\\[ | \\]$/g, '')");
        assertThat(o).isEqualTo("[ 'a', 'b', 'c' ], [ 'd', 'e', 'f' ]");
    }

    @Test
    public void testReplaceGetProp() {
        Object o = eval("String('fat').replace");
        assertThat(o).isNotNull();
        System.err.println("o: " + o);
    }

    @Test
    public void testReplaceOnce() {
        assertThat(eval("'If the police police the police...'.replace('police', 'sekretpolice')")).isEqualTo("If the sekretpolice police the police...");
    }

    @Test
    public void testReplaceOnceWithRegexp() {
        Object o = eval("String('fat freddie').replace(/f/, 'ph')");
        assertThat(o).isEqualTo("phat freddie");
    }

    @Test
    public void testReplaceGlobalWithRegexp() {
        Object o = eval("String('fat freddie').replace(/f/g, 'ph')");
        assertThat(o).isEqualTo("phat phreddie");
    }

    @Test
    public void testReplaceIgnoringCase() {
        Object o = eval("String('Now is the winter').replace(/n/i, '_N_')");
        assertThat(o).isEqualTo("_N_ow is the winter");
    }

    @Test
    public void testReplaceGloballyIgnoringCase() {
        Object o = eval("String('Now is the winter').replace(/n/ig, '_N_')");
        assertThat(o).isEqualTo("_N_ow is the wi_N_ter");
    }

    @Test
    public void testReplaceWithRegExpUsingAFunction() {
        eval("var upperToHyphenLower = function(match) { return '-'+match.toLowerCase(); }");
        Object o = eval("String('ABCDE abcde').replace(/[A-Z]/g, upperToHyphenLower)");
        assertThat(o).isEqualTo("-a-b-c-d-e abcde");
    }

    @Test
    public void testReplaceUsingAFunction() {
        eval("var upperToHyphenLower = function(match, arg2, arg3) { if (match) { return '-'+match.toLowerCase(); } else { return 'failure'; } }");
        Object o = eval("String('ABCDE abcde').replace('ABCDE', upperToHyphenLower)");
        assertThat(o).isEqualTo("-abcde abcde");
    }

    @Test
    public void testReplaceFunctionArg3() {
        eval("var __func = function(match, arg2, arg3) { return arg3; }");
        Object o = eval("String('ABCDE abcde').replace('BCDE', __func)");
        assertThat(o).isEqualTo("AABCDE abcde abcde");
    }

    @Test
    public void testReplaceFunctionArg2() {
        eval("var __func = function(match, arg2, arg3) { return arg2; }");
        Object o = eval("String('ABCDE abcde').replace('BCDE', __func)");
        assertThat(o).isEqualTo("A1 abcde");
    }

    @Test
    public void testReplaceFunctionArg1() {
        eval("var returnArg1 = function(match) { return match; }");
        Object o = eval("String('ABCDE abcde').replace(/[A-Z]/, returnArg1)");
        assertThat(o).isEqualTo("ABCDE abcde");
    }

    @Test
    public void testReplaceWithNoMatch() {
        assertThat(eval("String('abcde').replace(/[A-Z]/, 'foobar')")).isEqualTo("abcde");
    }

    @Test
    public void testReplaceWithTextSubstitution() {
        Object o = eval("\"$1,$2\".replace(/(\\$(\\d))/g, \"$$1-$1$2\")");
        assertThat(o).isEqualTo("$1-$11,$1-$22");
    }

    @Test
    public void testNoSubstitution() {
        assertThat(eval("new String('foobar').replace('oo', '$1')")).isEqualTo("f$1bar");
    }

    @Test
    public void testRegExpNoSubstitution() {
        assertThat(eval("new String('foobar').replace(/oo/, '$1')")).isEqualTo("f$1bar");
    }

    @Test
    public void testReplaceEmptyRegExp() {
        assertThat(eval("new String('asdf').replace(new RegExp('', 'g'), '1')")).isEqualTo("1a1s1d1f1");
    }

    @Test
    public void testReplaceDollarDollar() {
        Object o = eval("new String('foo').replace('foo', '$$')");
        assertThat(o).isEqualTo("$");
    }

    @Test
    public void testReplaceDollarAmpersand() {
        Object o = eval("new String('foo').replace(/o+/, '$&')");
        assertThat(o).isEqualTo("foo");
    }

    @Test
    public void testReplaceDollarBacktick() {
        Object o = eval("new String('foobar').replace(/oo/, '$`')");
        assertThat(o).isEqualTo("ffbar");
    }

    @Test
    public void testReplaceDollarApostrophe() {
        Object o = eval("new String('foobar').replace(/o+/, \"$'\")");
        assertThat(o).isEqualTo("fbarbar");
    }

    @Test
    public void testReplaceDollarN() {
        Object o = eval("new String('foobar').replace(/(fo+)(b.+)/, \"$2$1\")");
        assertThat(o).isEqualTo("barfoo");
    }

    @Test
    @Ignore
    public void testReplaceDollarNN() {
        Object o = eval("new String('supercalifragilisticexpialidocious').replace(/(s)(u)(p)(e)(r)(c)(a)(l)(i)(f)(r)(a)/, \"$11\")");
        assertThat(o).isEqualTo("rgilisticexpialidocious");
    }

    @Test
    public void testReplace_A1_T8() {
        eval("var __obj = {toString:function(){}};");
        assertThat(eval("String(__obj).replace(/e/g,void 0)")).isEqualTo("undundefinedfinundefinedd");
    }

    @Test
    public void testReplace_A4_T1() {
        eval("var __str = \"abc12 def34\";");
        eval("var __pattern = /([a-z]+)([0-9]+)/;");
        eval("function __replFN() { return arguments[2] + arguments[1]; }");
        assertThat(eval("__str.replace(__pattern, __replFN)")).isEqualTo("12abc def34");
    }

    @Test
    public void testReplace_A4_T2() {
        eval("var __str = \"abc12 def34\";");
        eval("var __pattern = /([a-z]+)([0-9]+)/g;");
        eval("function __replFN() { return arguments[2] + arguments[1]; }");
        assertThat(eval("__str.replace(__pattern, __replFN)")).isEqualTo("12abc 34def");
    }

    @Test
    public void testReplaceRegexContainingEscapedSlash() {
        Object o = eval("new String('foo/bar').replace(/\\//g, '\\\\/');");
        assertThat(o).isEqualTo("foo\\/bar");
    }

    @Test
    public void testReplaceRegexContainingUnescapedLeftBracket() {
        Object o = eval("new String('foozbar').replace(/o[^[]b/, 'o_b');");
        assertThat(o).isEqualTo("foo_bar");
    }

    @Test
    public void testValueOf() {
        Object o = eval("new String(42).valueOf()");
        assertThat(o).isEqualTo("42");
    }

    @Test
    public void testValueOfOnLiteral() {
        Object o = eval("'taco'.valueOf()");
        assertThat(o).isEqualTo("taco");
    }

    @Test
    public void testToString() {
        Object o = eval("new String(42).toString()");
        assertThat(o).isEqualTo("42");
    }

    @Test
    public void testToString_SpecWeirdness1() {
        Object o = eval("new Object().valueOf().toString()");
        assertThat(o).isEqualTo("[object Object]");
    }

    @Test
    public void testToString_SpecWeirdness2() {
        Object o = eval("var to_string_result = '[object '+ 'Object' +']';",
                "to_string_result.toString()");
        assertThat(o).isEqualTo("[object Object]");
    }

    @Test
    public void testCharAtWithinBounds() {
        Object o = eval("new String(42).charAt(1)");
        assertThat(o).isEqualTo("2");
    }

    @Test
    public void testCharAtOutsideBounds() {
        assertThat(eval("new String(42).charAt(2)")).isEqualTo("");
    }

    @Test(expected = ThrowException.class)
    public void testToStringMustThrowIfNotAPrimitiveResult() {
        eval("var obj = { toString:function(){ return new Object(); } };",
                "String(obj)");
    }

    @Test
    public void testCharCodeAt() {
        assertThat(eval("'BOB'.charCodeAt(2)")).isEqualTo(66L);
    }

    @Test
    public void testConcat() {
        assertThat(eval("'bob'.concat( 'lance', 'qmx' )")).isEqualTo("boblanceqmx");
    }

    @Test
    public void testIndexOf() {
        assertThat(eval("'bob'.indexOf('o')")).isEqualTo(1L);
    }

    @Test
    public void testIndexOfWithPos() {
        assertThat(eval("'bobobo'.indexOf('o',2)")).isEqualTo(3L);
    }

    @Test
    public void testIndexOfNotFound() {
        assertThat(eval("'bobobo'.indexOf('taco',2)")).isEqualTo(-1L);
    }

    @Test
    public void testLastIndexOf() {
        assertThat(eval("'bob'.lastIndexOf('b')")).isEqualTo(2L);
    }

    @Test
    public void testLastIndexOfWithPos() {
        assertThat(eval("'bob'.lastIndexOf('b',1)")).isEqualTo(0L);
    }

    @Test
    public void testLastIndexOfNotFound() {
        assertThat(eval("'bobobo'.lastIndexOf('taco',2)")).isEqualTo(-1L);
    }

    @Test
    public void testSearchString() {
        assertThat(eval("'boblanceqmx'.search('lance')")).isEqualTo(3L);
    }

    @Test
    public void testSearchRegExp() {
        assertThat(eval("'boblanceqmx'.search(/[ld]ance/)")).isEqualTo(3L);
    }

    @Test
    public void testSlice() {
        assertThat(eval("'boblanceqmx'.slice(3,8)")).isEqualTo("lance");
    }

    @Test
    public void testSliceWithUnaryPlusExpression() {
        assertThat(eval("var i = 7;\"{meters}m.othercrap\".slice(0, +i + 1 || 9e9)")).isEqualTo("{meters}");
    }

    @Test
    public void testSliceWithUnaryMinusExpression() {
        assertThat(eval("var i = 1;\"{meters}m.othercrap\".slice(0, -i + 9 || 9e9)")).isEqualTo("{meters}");
    }

    @Test
    public void testSplitString() {
        JSObject result = (JSObject) eval("'bob,lance,qmx'.split(',')");
        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("bob");
        assertThat(result.get(getContext(), "1")).isEqualTo("lance");
        assertThat(result.get(getContext(), "2")).isEqualTo("qmx");
    }

    @Test
    public void testSubstring() {
        assertThat(eval("'boblanceqmx'.substring(3,8)")).isEqualTo("lance");
    }

    @Test
    public void testSubstringNoEnd() {
        assertThat(eval("'boblanceqmx'.substring(3)")).isEqualTo("lanceqmx");
    }

    @Test
    public void testSubstr() {
        assertThat(eval("'boblanceqmx'.substr(0, 3)")).isEqualTo("bob");
    }

    @Test
    public void testSubstrZeroLength() {
        assertThat(eval("''.substr(2, 0)")).isEqualTo("");
    }

    @Test
    public void testSubstrFromEnd() {
        assertThat(eval("'boblanceqmx'.substr(-3)")).isEqualTo("qmx");
    }

    @Test
    public void testSubstrWithLength() {
        assertThat(eval("'boblanceqmx'.substr(-3, 2)")).isEqualTo("qm");
    }

    @Test
    public void testToLowerCase() {
        assertThat(eval("'BoBLaNcEqMX'.toLowerCase()")).isEqualTo("boblanceqmx");
    }

    @Test
    public void testToUpperCase() {
        assertThat(eval("'BoBLaNcEqMX'.toUpperCase()")).isEqualTo("BOBLANCEQMX");
    }

    @Test
    public void testTrim() {
        assertThat(eval("' bob\t'.trim()")).isEqualTo("bob");
    }

}
