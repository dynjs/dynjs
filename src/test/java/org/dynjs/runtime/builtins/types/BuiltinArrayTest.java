package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class BuiltinArrayTest extends AbstractDynJSTestSupport {

    @Test
    public void testArrayConstructorWithSizeZero() {
        eval("var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(0);
    }

    @Test
    public void testArrayConstructorWithSizeNonZero() {
        eval("var x = new Array(42);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(42);
    }

    @Test
    public void testArrayConstructorWithSizeNonNumeric() {
        eval("var x = new Array('taco');");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(1);
        assertThat(x.get(getContext(), "0")).isEqualTo("taco");
    }

    @Test
    public void testArrayConstructorWithElementList() {
        eval("var x = new Array(1,'lol', 2);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(3);
        assertThat(x.get(getContext(), "0")).isEqualTo(1);
        assertThat(x.get(getContext(), "1")).isEqualTo("lol");
        assertThat(x.get(getContext(), "2")).isEqualTo(2);
    }

    @Test
    public void testArrayInstancePrototype() {
        eval("var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.getPrototype()).isInstanceOf(DynArray.class);
        assertThat(x.getPrototype().get(null, "constructor")).isInstanceOf(BuiltinArray.class);
    }

    @Test
    public void testPrototype() {
        eval("var x = Array.prototype");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x).isNotNull();
    }

    @Test
    public void testPrototypeLikeLance() {
        eval("var x = (Array.prototype == null)");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isEqualTo(false);
    }

    @Test
    public void testArrayConstructor() {
        eval("var ctor = Array.prototype.constructor; var x = new ctor()");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.getPrototype()).isInstanceOf(DynArray.class);
        assertThat(x.getPrototype().get(null, "constructor")).isInstanceOf(BuiltinArray.class);
    }

    @Test
    public void testToString() {
        Object result = eval("new Array( 1, 2, 3 ).toString()");
        assertThat(result).isEqualTo("1,2,3");
    }

    @Test
    public void testJoinNoArg() {
        Object result = eval("new Array( 1, 2, 3 ).join()");
        assertThat(result).isEqualTo("1,2,3");
    }

    @Test
    public void testJoinWithArg() {
        Object result = eval("new Array( 1, 2, 3 ).join(' . ')");
        assertThat(result).isEqualTo("1 . 2 . 3");
    }

    @Test
    public void testJoinWithArgOnLiteral() {
        Object result = eval("[ 1, 2, 3 ].join(' . ')");
        assertThat(result).isEqualTo("1 . 2 . 3");

    }

    @Test
    public void testConcatNoSplat() {
        JSObject result = (JSObject) eval("[1,2,3].concat(4,5,6)");
        assertThat(result.get(getContext(), "length")).isEqualTo(6);
        assertThat(result.get(getContext(), "0")).isEqualTo(1);
        assertThat(result.get(getContext(), "1")).isEqualTo(2);
        assertThat(result.get(getContext(), "2")).isEqualTo(3);
        assertThat(result.get(getContext(), "3")).isEqualTo(4);
        assertThat(result.get(getContext(), "4")).isEqualTo(5);
        assertThat(result.get(getContext(), "5")).isEqualTo(6);
    }

    @Test
    public void testConcatWithSplat() {
        JSObject result = (JSObject) eval("[1,2,3].concat(4,[5,6],7)");
        assertThat(result.get(getContext(), "length")).isEqualTo(7);
        assertThat(result.get(getContext(), "0")).isEqualTo(1);
        assertThat(result.get(getContext(), "1")).isEqualTo(2);
        assertThat(result.get(getContext(), "2")).isEqualTo(3);
        assertThat(result.get(getContext(), "3")).isEqualTo(4);
        assertThat(result.get(getContext(), "4")).isEqualTo(5);
        assertThat(result.get(getContext(), "5")).isEqualTo(6);
        assertThat(result.get(getContext(), "6")).isEqualTo(7);
    }

    @Test
    public void testPop() {
        eval("var a = [ 'a', 'b', 'c', 'd', 'e' ]");

        assertThat(eval("a.length")).isEqualTo(5);

        assertThat(eval("a.pop()")).isEqualTo("e");
        assertThat(eval("a.length")).isEqualTo(4);

        assertThat(eval("a.pop()")).isEqualTo("d");
        assertThat(eval("a.length")).isEqualTo(3);

        assertThat(eval("a.pop()")).isEqualTo("c");
        assertThat(eval("a.length")).isEqualTo(2);

        assertThat(eval("a.pop()")).isEqualTo("b");
        assertThat(eval("a.length")).isEqualTo(1);

        assertThat(eval("a.pop()")).isEqualTo("a");
        assertThat(eval("a.length")).isEqualTo(0);

        assertThat(eval("a.pop()")).isEqualTo(Types.UNDEFINED);
        assertThat(eval("a.length")).isEqualTo(0);
    }

    @Test
    public void testPush() {
        eval("var a = ['a', 'b', 'c']");

        assertThat(eval("a.push( 'd', 'e' )")).isEqualTo(5);

        JSObject result = (JSObject) eval("a");
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
        assertThat(result.get(getContext(), "1")).isEqualTo("b");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
        assertThat(result.get(getContext(), "3")).isEqualTo("d");
        assertThat(result.get(getContext(), "4")).isEqualTo("e");

        assertThat(eval("a.length")).isEqualTo(5);
    }

    @Test
    public void testReverseEven() {
        JSObject result = (JSObject) eval("['a', 'b', 'c', 'd' ].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(4);
        assertThat(result.get(getContext(), "0")).isEqualTo("d");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
        assertThat(result.get(getContext(), "2")).isEqualTo("b");
        assertThat(result.get(getContext(), "3")).isEqualTo("a");
    }

    @Test
    public void testReverseOdd() {
        JSObject result = (JSObject) eval("['a', 'b', 'c', 'd', 'e' ].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(5);
        assertThat(result.get(getContext(), "0")).isEqualTo("e");
        assertThat(result.get(getContext(), "1")).isEqualTo("d");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
        assertThat(result.get(getContext(), "3")).isEqualTo("b");
        assertThat(result.get(getContext(), "4")).isEqualTo("a");
    }

    @Test
    public void testReverseOne() {
        JSObject result = (JSObject) eval("['a'].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(1);
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
    }

    @Test
    public void testShift() {
        eval("var a = [ 'a', 'b', 'c' ]");

        Object result = eval("a.shift()");

        assertThat(result).isEqualTo("a");
        
        JSObject a = (JSObject) eval( "a" );
        assertThat(a.get(getContext(), "length")).isEqualTo(2);
        assertThat(a.get(getContext(), "0")).isEqualTo("b");
        assertThat(a.get(getContext(), "1")).isEqualTo("c");
    }
    
    @Test
    public void testSlicePositiveArgs() {
        JSObject result = (JSObject) eval( "['a','b','c','d'].slice(1,3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(2);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
    }
    
    @Test
    public void testSliceNegativeEnd() {
        JSObject result = (JSObject) eval( "['a','b','c','d'].slice(0,-1)");
        assertThat(result.get(getContext(), "length")).isEqualTo(3);
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
        assertThat(result.get(getContext(), "1")).isEqualTo("b");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
    }
    
    @Test
    public void testSliceNegativeStart() {
        JSObject result = (JSObject) eval( "['a','b','c','d'].slice(-3,3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(2);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
    }
    
    @Test
    public void testSliceNegativeStartNoEnd() {
        JSObject result = (JSObject) eval( "['a','b','c','d'].slice(-3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(3);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
        assertThat(result.get(getContext(), "2")).isEqualTo("d");
    }
    
    @Test
    public void testForEach() {
        JSObject result = (JSObject) eval( "var collector = [];",
                "['a', 'b', 'c' ].forEach( function(each,i) {",
                "  collector.push('foo'+each);",
                "} );",
                "collector;");
        
        assertThat( result.get(getContext(), "length" ) ).isEqualTo(3);
        assertThat(result.get(getContext(), "0")).isEqualTo("fooa");
        assertThat(result.get(getContext(), "1")).isEqualTo("foob");
        assertThat(result.get(getContext(), "2")).isEqualTo("fooc");
    }
    
}
