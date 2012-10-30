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
    public void testReplace() {
        Object o = eval("String('fat').replace('f', 'ph')");
        assertThat(o).isEqualTo("phat");
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
        eval("var upperToHyphenLower = function(match) { return '-'+match.toLowerCase(); }");
        Object o = eval("String('ABCDE abcde').replace('ABCDE', upperToHyphenLower)");
        assertThat(o).isEqualTo("-abcde abcde");
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
        Object o = eval( "new Object().valueOf().toString()");
        assertThat( o ).isEqualTo( "[object Object]" );
    }
    
    @Test
    public void testToString_SpecWeirdness2() {
        Object o = eval( "var to_string_result = '[object '+ 'Object' +']';",
                "to_string_result.toString()" );
        assertThat( o ).isEqualTo( "[object Object]" );
    }


    @Test
    public void testCharAtWithinBounds() {
        Object o = eval("new String(42).charAt(1)");
        assertThat(o).isEqualTo("2");
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
        assertThat( eval( "'bob'.lastIndexOf('b')")).isEqualTo(2L);
    }
    
    @Test
    public void testLastIndexOfWithPos() {
        assertThat( eval( "'bob'.lastIndexOf('b',1)")).isEqualTo(0L);
    }
    
    @Test
    public void testLastIndexOfNotFound() {
        assertThat(eval("'bobobo'.lastIndexOf('taco',2)")).isEqualTo(-1L);
    }
    
    @Test
    public void testSearchString() {
        assertThat( eval( "'boblanceqmx'.search('lance')")).isEqualTo(3L);
    }
    
    @Test
    public void testSearchRegExp() {
        assertThat( eval( "'boblanceqmx'.search(/[ld]ance/)")).isEqualTo(3L);
    }
    
    @Test
    public void testSlice() {
        assertThat( eval( "'boblanceqmx'.slice(3,8)")).isEqualTo("lance");
    }
    
    @Test
    public void testSplitString() {
        JSObject result = (JSObject) eval( "'bob,lance,qmx'.split(',')" );
        assertThat( result.get( getContext(), "length" ) ).isEqualTo(3L);
        assertThat( result.get( getContext(), "0" ) ).isEqualTo("bob" );
        assertThat( result.get( getContext(), "1" ) ).isEqualTo("lance" );
        assertThat( result.get( getContext(), "2" ) ).isEqualTo("qmx" );
    }
    
    @Test
    public void testSubstring() {
        assertThat( eval( "'boblanceqmx'.substring(3,8)")).isEqualTo("lance");
    }
    
    @Test
    public void testSubstringNoEnd() {
        assertThat( eval( "'boblanceqmx'.substring(3)")).isEqualTo("lanceqmx");
    }
    
    @Test
    public void testToLowerCase() {
        assertThat( eval( "'BoBLaNcEqMX'.toLowerCase()")).isEqualTo("boblanceqmx");
    }
    
    @Test
    public void testToUpperCase() {
        assertThat( eval( "'BoBLaNcEqMX'.toUpperCase()")).isEqualTo("BOBLANCEQMX");
    }

    
    @Test
    public void testTrim()  {
        assertThat( eval( "' bob\t'.trim()")).isEqualTo("bob");
    }
    
}
