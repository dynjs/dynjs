package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.*;
import org.junit.Test;

public class BuiltinArrayTest extends AbstractDynJSTestSupport {

    @Test
    public void testArrayConstructorWithSizeZero() {
        eval("var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(0L);
    }

    @Test
    public void testArrayConstructorWithSizeNonZero() {
        eval("var x = new Array(42);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(42L);
    }
    
    @Test
    public void testArrayFunctionWithSizeNonZero() {
        eval("var x = Array(42);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(42L);
    }

    @Test
    public void testArrayConstructorWithSizeNonNumeric() {
        eval("var x = new Array('taco');");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(1L);
        assertThat(x.get(getContext(), "0")).isEqualTo("taco");
    }

    @Test
    public void testArrayConstructorWithElementList() {
        eval("var x = new Array(1,'lol', 2);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(3L);
        assertThat(x.get(getContext(), "0")).isEqualTo(1L);
        assertThat(x.get(getContext(), "1")).isEqualTo("lol");
        assertThat(x.get(getContext(), "2")).isEqualTo(2L);
    }
    
    @Test
    public void testArrayFunctionWithElementList() {
        eval("var x = Array(1,'lol', 2);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "length")).isEqualTo(3L);
        assertThat(x.get(getContext(), "0")).isEqualTo(1L);
        assertThat(x.get(getContext(), "1")).isEqualTo("lol");
        assertThat(x.get(getContext(), "2")).isEqualTo(2L);
    }


    @Test
    public void testArrayInstancePrototype() {
        eval("var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.getPrototype()).isInstanceOf(DynArray.class);
        JSObject prototype = (JSObject) x.getPrototype();
        assertThat(prototype.get(null, "constructor")).isInstanceOf(BuiltinArray.class);
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
        JSObject prototype = (JSObject) x.getPrototype();
        assertThat(prototype.get(null, "constructor")).isInstanceOf(BuiltinArray.class);
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
        assertThat(result.get(getContext(), "length")).isEqualTo(6L);
        assertThat(result.get(getContext(), "0")).isEqualTo(1L);
        assertThat(result.get(getContext(), "1")).isEqualTo(2L);
        assertThat(result.get(getContext(), "2")).isEqualTo(3L);
        assertThat(result.get(getContext(), "3")).isEqualTo(4L);
        assertThat(result.get(getContext(), "4")).isEqualTo(5L);
        assertThat(result.get(getContext(), "5")).isEqualTo(6L);
    }

    @Test
    public void testConcatWithSplat() {
        JSObject result = (JSObject) eval("[1,2,3].concat(4,[5,6],7)");
        assertThat(result.get(getContext(), "length")).isEqualTo(7L);
        assertThat(result.get(getContext(), "0")).isEqualTo(1L);
        assertThat(result.get(getContext(), "1")).isEqualTo(2L);
        assertThat(result.get(getContext(), "2")).isEqualTo(3L);
        assertThat(result.get(getContext(), "3")).isEqualTo(4L);
        assertThat(result.get(getContext(), "4")).isEqualTo(5L);
        assertThat(result.get(getContext(), "5")).isEqualTo(6L);
        assertThat(result.get(getContext(), "6")).isEqualTo(7L);
    }
    
    @Test
    public void testConcatWithNoArgs() {
        eval("var x = [1,2,3]");
        eval("var y = x.concat()");
        assertThat(eval("y.length")).isEqualTo(3L);
        // TODO: Strict equality on arrays fails
//        assertThat(eval("y === x")).isEqualTo(true);
    }
    
    @Test
    public void testElidedArrays() {
        DynArray result = (DynArray) eval( "[,]" );
        System.err.println( result.get( getContext(), "length" ) );
    }

    @Test
    public void testConcatWithEmptyArrays() {
        eval("var x = [,1]");
        eval("var y = x.concat([],[,])");
        assertThat(eval("y[0]")).isEqualTo(Types.UNDEFINED);
        assertThat(eval("y[1]")).isEqualTo(1L);
        assertThat(eval("y[2]")).isEqualTo(Types.UNDEFINED);
        assertThat(eval("y.length")).isEqualTo(3L);
    }
    
    @Test
    public void testPop() {
        eval("var a = [ 'a', 'b', 'c', 'd', 'e' ]");

        assertThat(eval("a.length")).isEqualTo(5L);

        assertThat(eval("a.pop()")).isEqualTo("e");
        assertThat(eval("a.length")).isEqualTo(4L);

        assertThat(eval("a.pop()")).isEqualTo("d");
        assertThat(eval("a.length")).isEqualTo(3L);

        assertThat(eval("a.pop()")).isEqualTo("c");
        assertThat(eval("a.length")).isEqualTo(2L);

        assertThat(eval("a.pop()")).isEqualTo("b");
        assertThat(eval("a.length")).isEqualTo(1L);

        assertThat(eval("a.pop()")).isEqualTo("a");
        assertThat(eval("a.length")).isEqualTo(0L);

        assertThat(eval("a.pop()")).isEqualTo(Types.UNDEFINED);
        assertThat(eval("a.length")).isEqualTo(0L);
    }

    @Test
    public void testPush() {
        eval("var a = ['a', 'b', 'c']");

        assertThat(eval("a.push( 'd', 'e' )")).isEqualTo(5L);

        JSObject result = (JSObject) eval("a");
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
        assertThat(result.get(getContext(), "1")).isEqualTo("b");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
        assertThat(result.get(getContext(), "3")).isEqualTo("d");
        assertThat(result.get(getContext(), "4")).isEqualTo("e");

        assertThat(eval("a.length")).isEqualTo(5L);
        
    }
    
    @Test
    public void testPushReturnsCorrectLengthEvenWhenNothingIsPushed() {
        assertThat(eval("var x = new Array(); x.push()")).isEqualTo(0L);
    }

    @Test
    public void testReverseEven() {
        JSObject result = (JSObject) eval("['a', 'b', 'c', 'd' ].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(4L);
        assertThat(result.get(getContext(), "0")).isEqualTo("d");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
        assertThat(result.get(getContext(), "2")).isEqualTo("b");
        assertThat(result.get(getContext(), "3")).isEqualTo("a");
    }

    @Test
    public void testReverseOdd() {
        JSObject result = (JSObject) eval("['a', 'b', 'c', 'd', 'e' ].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(5L);
        assertThat(result.get(getContext(), "0")).isEqualTo("e");
        assertThat(result.get(getContext(), "1")).isEqualTo("d");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
        assertThat(result.get(getContext(), "3")).isEqualTo("b");
        assertThat(result.get(getContext(), "4")).isEqualTo("a");
    }

    @Test
    public void testReverseOne() {
        JSObject result = (JSObject) eval("['a'].reverse()");

        assertThat(result.get(getContext(), "length")).isEqualTo(1L);
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
    }

    @Test
    public void testShift() {
        eval("var a = [ 'a', 'b', 'c' ]");

        Object result = eval("a.shift()");

        assertThat(result).isEqualTo("a");

        JSObject a = (JSObject) eval("a");
        assertThat(a.get(getContext(), "length")).isEqualTo(2L);
        assertThat(a.get(getContext(), "0")).isEqualTo("b");
        assertThat(a.get(getContext(), "1")).isEqualTo("c");
    }

    @Test
    public void testShiftEmptyArray() {
        eval("var a = []");

        Object result = eval("a.shift()");

        assertThat(result).isEqualTo(Types.UNDEFINED);

        JSObject a = (JSObject) eval("a");
        assertThat(a.get(getContext(), "length")).isEqualTo(0L);
    }

    @Test
    public void testSlicePositiveArgs() {
        JSObject result = (JSObject) eval("['a','b','c','d'].slice(1,3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(2L);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
    }

    @Test
    public void testSliceNegativeEnd() {
        JSObject result = (JSObject) eval("['a','b','c','d'].slice(0,-1)");
        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
        assertThat(result.get(getContext(), "1")).isEqualTo("b");
        assertThat(result.get(getContext(), "2")).isEqualTo("c");
    }

    @Test
    public void testSliceNegativeStart() {
        JSObject result = (JSObject) eval("['a','b','c','d'].slice(-3,3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(2L);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
    }

    @Test
    public void testSliceNegativeStartNoEnd() {
        JSObject result = (JSObject) eval("['a','b','c','d'].slice(-3)");
        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("b");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
        assertThat(result.get(getContext(), "2")).isEqualTo("d");
    }

    @Test
    public void testForEach() {
        JSObject result = (JSObject) eval("var collector = [];",
                "['a', 'b', 'c' ].forEach( function(each,i) {",
                "  collector.push('foo'+each);",
                "} );",
                "collector;");

        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("fooa");
        assertThat(result.get(getContext(), "1")).isEqualTo("foob");
        assertThat(result.get(getContext(), "2")).isEqualTo("fooc");
    }

    @Test
    public void testMap() {
        JSObject result = (JSObject) eval(
                "['a', 'b', 'c' ].map( function(each,i) {",
                "  return 'foo' + each;",
                "} );");

        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("fooa");
        assertThat(result.get(getContext(), "1")).isEqualTo("foob");
        assertThat(result.get(getContext(), "2")).isEqualTo("fooc");
    }

    @Test
    public void testFilter() {
        JSObject result = (JSObject) eval(
                "['a', 'b', 'c', 'd' ].filter( function(each,i) {",
                "  return ( i % 2 == 0 );",
                "} );");

        assertThat(result.get(getContext(), "length")).isEqualTo(2L);
        assertThat(result.get(getContext(), "0")).isEqualTo("a");
        assertThat(result.get(getContext(), "1")).isEqualTo("c");
    }

    @Test
    public void testSortStringsNoFunction() {
        JSObject result = (JSObject) eval("['qmx', 'lance', 'bob' ].sort()");

        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("bob");
        assertThat(result.get(getContext(), "1")).isEqualTo("lance");
        assertThat(result.get(getContext(), "2")).isEqualTo("qmx");
    }

    @Test
    public void testSortNonStringsNoFunction() {
        JSObject result = (JSObject) eval("[1,2,10,20].sort()");
        assertThat(result.get(getContext(), "length")).isEqualTo(4L);
        assertThat(result.get(getContext(), "0")).isEqualTo(1L);
        assertThat(result.get(getContext(), "1")).isEqualTo(10L);
        assertThat(result.get(getContext(), "2")).isEqualTo(2L);
        assertThat(result.get(getContext(), "3")).isEqualTo(20L);
    }

    @Test
    public void testStringsWithFunction() {
        JSObject result = (JSObject) eval("[ 'thebob', 'lance', 'qmx' ].sort( function(x,y){",
                "if ( x.length > y.length ) {",
                "  return 1;",
                "} else if ( x.length < y.length ) {",
                "  return -1;",
                "} else {",
                "  return 0",
                "} } )");

        assertThat(result.get(getContext(), "length")).isEqualTo(3L);
        assertThat(result.get(getContext(), "0")).isEqualTo("qmx");
        assertThat(result.get(getContext(), "1")).isEqualTo("lance");
        assertThat(result.get(getContext(), "2")).isEqualTo("thebob");
    }

    @Test
    public void testSpliceNoDeletion() {
        JSObject result = (JSObject) eval("var a = [1,2,3];",
                "a.splice(0,0,4,5)");

        JSObject a = (JSObject) eval("a");

        assertThat(a.get(getContext(), "length")).isEqualTo(5L);
        assertThat(a.get(getContext(), "0")).isEqualTo(4L);
        assertThat(a.get(getContext(), "1")).isEqualTo(5L);
        assertThat(a.get(getContext(), "2")).isEqualTo(1L);
        assertThat(a.get(getContext(), "3")).isEqualTo(2L);
        assertThat(a.get(getContext(), "4")).isEqualTo(3L);

        assertThat(result.get(getContext(), "length")).isEqualTo(0L);
    }

    @Test
    public void testSpliceWithDeletion() {
        JSObject result = (JSObject) eval("var a = [1,2,3];",
                "a.splice(0,1,4,5)");

        JSObject a = (JSObject) eval("a");

        assertThat(a.get(getContext(), "length")).isEqualTo(4L);
        assertThat(a.get(getContext(), "0")).isEqualTo(4L);
        assertThat(a.get(getContext(), "1")).isEqualTo(5L);
        assertThat(a.get(getContext(), "2")).isEqualTo(2L);
        assertThat(a.get(getContext(), "3")).isEqualTo(3L);

        assertThat(result.get(getContext(), "length")).isEqualTo(1L);
        assertThat(result.get(getContext(), "0")).isEqualTo(1L);
    }

    @Test
    public void testUnshift() {
        Object result = eval("var a = [1,2,3];",
                "a.unshift(4,5,6,7)");

        JSObject a = (JSObject) eval("a");

        assertThat(result).isEqualTo(7L);

        assertThat(a.get(getContext(), "length")).isEqualTo(7L);
        assertThat(a.get(getContext(), "0")).isEqualTo(4L);
        assertThat(a.get(getContext(), "1")).isEqualTo(5L);
        assertThat(a.get(getContext(), "2")).isEqualTo(6L);
        assertThat(a.get(getContext(), "3")).isEqualTo(7L);
        assertThat(a.get(getContext(), "4")).isEqualTo(1L);
        assertThat(a.get(getContext(), "5")).isEqualTo(2L);
        assertThat(a.get(getContext(), "6")).isEqualTo(3L);
    }

    @Test
    public void testIndexOfNoStart() {
        Object result = eval("['bob', 'lance', 'qmx'].indexOf( 'lance')");
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void testIndexOfWithPositiveStart() {
        Object result = eval("['bob', 'lance', 'qmx', 'bob', 'lance', 'qmx' ].indexOf('lance',2)");
        assertThat(result).isEqualTo(4L);
    }

    @Test
    public void testIndexOfWithNegativeStart() {
        Object result = eval("['bob', 'lance', 'qmx', 'bob', 'lance', 'qmx' ].indexOf('lance',-3)");
        assertThat(result).isEqualTo(4L);
    }
    
    @Test
    public void testIndexOfWithLargeArrays() {
        eval("var a = new Array(0,1);",
                "a[4294967294] = 2;",
                "a[4294967295] = 3;",
                "a[4294967296] = 4;",
                "a[4294967297] = 5;");
        assertThat(eval("a.indexOf(2,4294967290) === 4294967294")).isEqualTo(true);
        assertThat(eval("a.indexOf(3,4294967290) === -1")).isEqualTo(true);
    }
    
    @Test
    public void testLastIndexOfWithFromIndexUndefined() {
        eval("var a = new Array(1,2,1);");
        assertThat(eval("a.lastIndexOf(2, undefined) === -1")).isEqualTo(true);
        assertThat(eval("a.lastIndexOf(1, undefined) ==- 0")).isEqualTo(true);
        assertThat(eval("a.lastIndexOf(1) === 2")).isEqualTo(true);
    }

    @Test
    public void testLastIndexOfNoStart() {
        Object result = eval("['bob', 'lance', 'qmx'].lastIndexOf( 'lance')");
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void testLastIndexOfWithPositiveStart() {
        Object result = eval("['bob', 'lance', 'qmx', 'bob', 'lance', 'qmx' ].lastIndexOf('lance',3)");
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void testLastIndexOfWithNegativeStart() {
        Object result = eval("['bob', 'lance', 'qmx', 'bob', 'lance', 'qmx' ].lastIndexOf('lance',-3)");
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void testLastIndexOfWithInfinity() {
        Object result = eval("var obj = { 0: 0, length: Infinity };",
                "Array.prototype.lastIndexOf.call(obj, 0) === -1;");

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testEveryTrue() {
        Object result = eval("[1,2,3].every(function(e){ return e < 10 })");
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testEveryFalse() {
        Object result = eval("[1,2,3,4,5,11,6,7,8,9].every(function(e){ return e < 10 })");
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void testSomeTrue() {
        Object result = eval("[1,2,3].some(function(e){ return e > 2 })");
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testSomeFalse() {
        Object result = eval("[1,2,3,4,5,11,6,7,8,9].some(function(e){ return e > 40 })");
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void testReduce() {
        Object result = eval("[1,2,3].reduce( function(accum,e){ return accum+e } )");
        assertThat(result).isEqualTo(6L);
    }

    @Test
    public void testReduceWithInitial() {
        Object result = eval("[1,2,3].reduce( function(accum,e){ return accum+e }, 10 )");
        assertThat(result).isEqualTo(16L);
    }

    @Test
    public void testReduceRight() {
        Object result = eval("[1,2,3].reduceRight( function(accum,e){ return accum+e } )");
        assertThat(result).isEqualTo(6L);
    }

    @Test
    public void testReduceRightWithInitial() {
        Object result = eval("[1,2,3].reduceRight( function(accum,e){ return accum+e }, 10 )");
        assertThat(result).isEqualTo(16L);
    }

    @Test
    public void testReduceRightSpec1() {
        Object result = eval("var initialValue = 0;",
                "function callbackfn(accum, val, idx, obj) {",
                "    accum += val;",
                "    return accum;",
                "}",
                "20 === [11, 9].reduceRight(callbackfn, initialValue);");
        
        assertThat( result ).isEqualTo(true);
    }
    
    @Test
    public void testToLocaleString() {
        assertThat(eval("var n = 0;",
            "var obj = {toLocaleString: function() {n++}};",
            "var arr = [undefined, obj, null, obj, obj];",
            "arr.toLocaleString();",
            "n")).isEqualTo(3L);
    }
    
    @Test
    public void testTypeofArray() {
        eval("var arr = ['b','a','c','o','n']");
        assertThat(eval("typeof arr")).isEqualTo("object");
        assertThat(eval("arr instanceof Array")).isEqualTo(true);
        eval("function foo() { return arguments[0] instanceof Array; }");
        assertThat(eval("foo(['p','i','g'])")).isEqualTo(true);
    }

    @Test
    public void testArraySubclass() {
        eval("function BetterArray() { Array.apply(this, arguments); }");
        eval("BetterArray.prototype = Object.create(Array.prototype, {" +
                "constructor: {" +
                "  value: BetterArray," +
                "  enumerable: false," +
                "  writable: true," +
                "  configurable: true }" +
                "});");
        eval("var x = new BetterArray(1,2,3,4,5);");
        assertThat(eval("x.length")).isEqualTo(5L);
        assertThat(eval("x[0]")).isEqualTo(1L);
        assertThat(eval("x[3]")).isEqualTo(4L);
    }

}
