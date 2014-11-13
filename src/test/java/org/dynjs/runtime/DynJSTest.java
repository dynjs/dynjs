/**
 *  Copyright 2013 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.DynJSException;
import org.dynjs.exception.ThrowException;
import org.junit.Ignore;
import org.junit.Test;

public class DynJSTest extends AbstractDynJSTestSupport {

    @Test
    public void testOperatorPrecedence() {
        assertThat( eval( "3-1+1" ) ).isEqualTo(3L);
        assertThat( eval( "3-0+1" ) ).isEqualTo(4L);
        assertThat( eval( "3-2*2" ) ).isEqualTo(-1L);
        assertThat( eval( "3*2-2" ) ).isEqualTo(4L);
    }
    
    @Test
    public void testObjectPrototypePrototypeIsNull() {
        assertThat(eval("Object.getPrototypeOf(Object.prototype) === null")).isEqualTo(true);
    }
    
    @Test
    public void testTypeOfUndefined() {
        assertThat(eval("typeof(undefined) === 'undefined'")).isEqualTo(true);
    }
    
    @Test
    public void testObject() {
        assertThat(eval("eval('{}')")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test(expected = ThrowException.class)
    public void testSyntaxErrorThrows() {
        getRuntime().execute("var f ( {;");
    }

    @Test
    public void evalLines() {
        getRuntime().evaluate(
                "var x = 'test'",
                "var y = x");
        Reference y = getContext().resolve("y");
        assertThat(y)
                .isNotNull()
                .isInstanceOf(Reference.class);
        assertThat(y.getValue(getContext()))
                .isEqualTo("test");
    }

    @Test
    public void assignsGlobalVariables() {
        check("var result = 'test';", "test");
        check("var result = undefined; result = 1.0;", 1L);
    }

    @Test
    public void defineUnInitializedGlobalVariables() {
        eval("var x;");
        Reference x = getContext().resolve("x");
        Object val = x.getValue(getContext());
        assertThat(val)
                .isNotNull()
                .isEqualTo(Types.UNDEFINED);
    }

    @Test
    public void assignsExprGlobalVariables() {
        check("var x = 2 + 1; var result = x == 3;");
    }

    @Test
    public void assignsExprMulGlobalVariables() {
        check("var x = 3 * 2; var result = x == 6;");
    }

    @Test
    public void assignsExprSubGlobalVariables() {
        check("var x = 3 - 1; var result = x == 2;");
    }

    @Test
    public void assignsComplexExprSubGlobalVariables() {
        check("var x = 3 * 2 - 1; var result = x == 5");
    }

    @Test
    public void assignsComplexparExprSubGlobalVariables() {
        check("var x = (3 * 2) - 1; var result = x == 5");
    }

    @Test
    public void assignsNamedEmptyFunction() {
        check("function x(){}; var result = x();", Types.UNDEFINED);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        check("var x = function(a,b,c){}; var result = x();", Types.UNDEFINED);
    }

    @Test
    public void buildFunctionWithBody() {
        check("var result = (function(a,b){var w = (1 + 2) * 3;})();", Types.UNDEFINED);
    }

    @Test
    public void buildFunctionWithMultipleStatementBody() {
        check("var result = (function(){var a = 1;var b = 2; var c = a + b;})()", Types.UNDEFINED);
    }

    @Test
    public void buildFunctionWithReturn() {
        check("var result = (function(){return 1+1;})() === 2;");
    }

    @Test(expected = ThrowException.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        eval("print(x);");
    }

    @Test
    public void testPrint() {
        eval("print('taco');");
    }

    @Test
    public void testRelationalOperators() {
        check("var result = 1 < 2;");
        check("var result = 2 > 1;");
        check("var result = 2 <= 2;");
        check("var result = 2 >= 2;");
    }

    @Test
    public void testBooleanLiterals() {
        check("var result = true;", true);
        check("var result = false;", false);
    }

    @Test
    public void testMathOperations() {
        //check("var x = 1 + 1;var result = x == 2");
        //check("var x = 1 - 1;var result = x == 0");
        //check("var x = 3 * 7;var result = x == 21");
        check("var x = 3 / 2;var result = x == 1.5");
        //check("var x = 3 % 2;var result = x == 1");
    }

    @Test
    public void testHexValue() {
        check("var result = 0x16", 22L);
    }

    @Test
    public void testBitwise() {
        check("var result = 0x000F & 0x2222", 2L);
        check("var result = 0x000F | 0x2222", 8751L);
        check("var result = 0x000F ^ 0x2222", 8749L);
        check("var result = 0x000F; result &= 0x2222", 2L);
        check("var result = 0x000F; result |= 0x2222", 8751L);
        check("var result = 0x000F; result ^= 0x2222", 8749L);

        check("var result = 9 << 2", 36);
        check("var result = 9 >> 2", 2);
        check("var result = -1 >>> 2", 1073741823L);
        check("var result = 9; result <<= 2", 36);
        check("var result = 9; result >>= 2", 2);
        check("var result = -1; result >>>= 2", 1073741823L);
    }

    @Test
    public void testAssignments() {
        check("var x = 1;x += 1; var result = x == 2;");
        check("var x = 1;x -= 1; var result = x == 0;");
        check("var x = 1;x *= 3; var result = x == 3;");
        check("var x = 1;x /= 1; var result = x == 1.0;");
        check("var x = 2;x %= 1; var result = x == 0;");
    }

    @Test
    public void testContinue() {
        eval("var x = 0;",
                "for (var i = 0;i < 10; i+=1){",
                "  continue;",
                "  x+=1;",
                "}");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isEqualTo(0L);

        eval("var x = 0;",
                "do {",
                "  x+=1;",
                "  if(x % 3 == 0) {",
                "    continue;",
                "  }",
                "  x+=3;",
                "} while(x < 10)");
        x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isEqualTo(13L);

        eval("var x = 0;",
                "while(x < 10) {",
                "  x+=1;",
                "  if(x % 2 == 0) {",
                "    continue;",
                "  }",
                "  ;x+=3",
                "}");
        x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isEqualTo(12L);
    }

    @Test
    public void testBreak() {
        check("var x = 0; for (var i = 0;i < 10; i+=1){ x+=1; break;}; var result = x == 1;");
        check("var x = 0; var i = 0; for (;i < 10; i+=1){ x+=1; break;}; var result = x == 1;");
        check("var x = 0; do { x+=1;if(x % 3 == 0) {break;};x+=3 } while(x < 10); var result = x == 9;");
        check("var x = 0; while(x < 10) { x+=1; if(x % 2 == 0) {break;};x+=3}; var result = x == 12;");
    }

    @Test
    public void testNullLiteral() {
        Object result = eval("null");
        assertThat(result).isEqualTo(Types.NULL);
    }

    @Test
    public void testTernaryOperator() {
        check("var x = 1 > 2 ? 55 : 56; var result = x == 56");
    }

    @Test
    public void testLogicalOperators() {
        check("var result = false || true;", true);
        check("var result = true || false;", true);
        check("var result = false || false;", false);
        check("var result = true && false;", false);
        check("var result = false && true;", false);
        check("var result = true && true;", true);
    }

    @Test
    public void testEmptyObjectLiteral() {
        final Object result = resultFor("var result = {};");
        assertThat(result)
                .isNotNull()
                .isInstanceOf(JSObject.class);
    }

    /*
    @Test
    public void testBasicObjectLiteral() {
        final String expression = "var result = {w:true};";
        final Object result = resultFor(expression);
        assertThat(result)
                .isNotNull()
                .isInstanceOf(DynObject.class);
        assertThat(((DynObject) result).resolve("w")).isInstanceOf(Boolean.class).isEqualTo(Boolean.TRUE);
    }
     */

    @Test
    public void testObjectLiteralPropertyAccess() {
        check("var x = {w:true}; var result = x.w;", true);
        check("var x = {'y':false}; var result = x.y;", false);
        check("var x = {'z':true}; var result = x['z'];", true);
    }

    @Test
    public void testObjectAssignment() {
        check("var x = {}; x.a = 'lol'; var result = x.a == 'lol';");
    }

    /*
    @Test
    public void testBuiltinLoading() {
        getConfig().addBuiltin("sample", DynJSCompiler.wrapFunction(getContext(), new BypassFunction()));
        check("var result = sample(true);");
    }
     */

    @Test
    public void testEval() {
        Object result = eval("eval('4+2')");
        assertThat(result).isEqualTo(6L);
    }

    @Test
    public void testTypeOf() {
        check("var result = typeof undefined;", "undefined");
        check("var result = typeof null === 'object';");
        check("var result = typeof {} === 'object';");
        check("var result = typeof true === 'boolean';");
        check("var result = typeof 1.0 === 'number';");
        check("var result = typeof 'lol' === 'string';");
        check("function x(){}; var result = typeof x === 'function';");
    }

    @Test
    public void testInstanceOf() {
        check("function Car(){}; var x = new Car; var result = x instanceof Car;");
    }

    @Test
    public void testVoid() {
        check("var result = undefined === undefined;");
    }

    @Test
    public void testThis() {
        check("var x = {name:'myName', lol:function(){return this.name;}}; var result = x.name == 'myName' && x.lol() == 'myName';");
    }

    @Test(expected = DynJSException.class)
    public void testThrow() {
        check("throw 'pizza';");
    }

    @Test
    public void testDeleteOper() {
        check("var x = {a:'lol'}; var result = delete x.a;", true);
        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        assertThat(x.get(getContext(), "a")).isEqualTo(Types.UNDEFINED);

    }

    @Test
    public void testUnaryMinusOper() {
        check("var result = -1 + 1", 0L);
        check("var result = -1", -1L);
        check("var x = 1; var result = -x == -1");
    }

    @Test
    public void testIfStatement() {
        check("var x = function(a) { if (a) { return 1; } else { return 2; } }; var result = x(true) == 1;");
    }

    @Test
    public void testFunctionCall() {
        check("var result = (function () { return true; } )();");
        check("function testCall(a) { return a; }; var result = testCall(1) == 1;");
    }

    @Test
    public void testFactorial() {
        check("function factorial(n) { if (n == 1) { return 1; } else { return (n * factorial(n - 1)); } }; var result = factorial(5) == 120;");
    }

    @Test
    public void testFibRecursive() {
        check("function fib(n) { return n < 2 ? n : fib(n - 1) + fib(n - 2); }; var result = fib(6) == 8;");
    }

    @Test
    public void testIncrement() {
        check("var x = 1; var y = 1; var result = ++x == 2 && y++ == 1;");
    }

    @Test
    public void testDecrement() {
        check("var x = 2; var y = 2; var result = --x == 1 && y-- == 2;");
    }

    @Test
    public void testMath() {
        check("var x = 10; var result = (x + 5 == 15) && (x - 1 == 9) && (x * 3 == 30) && (x / 2 == 5.0) && (x % 3 == 1);");
    }

    @Test
    public void testRelational() {
        check("var x = 10; var result = (x > 9) && (x < 11) && (x <= 10) && (x >= 10);");
    }

    @Test
    public void testEquality() {
        // equality
        check("var result = undefined == undefined;");
        check("var result = null == null;");
        check("var result = NaN == NaN;", false);
        check("var result = 1 == 1;");
        check("var result = 0 == -0;");
        check("var result = -0 == 0;");
        check("var result = null == undefined;");
        check("var result = undefined == null;");
        check("var result = 1 == '1';");
        check("var result = '1' == 1;");
        check("var result = '' == 0");
        check("var result = true == 1;");
        check("var result = false == 0;");
        check("var result = 1 == true;");
        check("var result = 0 == false;");
        check("var result = 1 != 2;");

        // strict equality
        check("var result = 1 === 1;");
        check("var result = 'a' === 'a';");
        check("var result = true === true;");
        check("var x = 'foo'; var y = x; result = x === y;");
        check("var result = 1 !== 2;");
    }

    @Test
    public void testLoop() {
        check("var x = 10;var y = 0; while(x < 10){x+=1;y+=1}; var result = y == 0;");
        check("var x = 10;var y = 0; do { x+=1;y+=1; } while(x < 10); var result = y == 1;");
        check("var x = 10; var y = 0; while(x < 10){ x += 1; y += 1 }; var result = y == 0;");
    }

    @Test
    public void testFor() {
        check("var x = 0; for (var i =0;i < 10; i+=1){ x+=1;}; var result = x == 10");
        check("var x = 0; var i =0; for (var w = 0;i < 10; i+=1){ x+=1;}; var result = i == 10");
        check("var x = 0; for (; x < 10; x += 1) { x += 1; }; var result = x == 10");
        check("var i = 0; var x = 33; for(; i < 10; i++) { x -= 1} var result = x == 23");
        check("var x = 0; for (var i =0; i < 10; i += 1) { x += 1; } var result = x == 10;");
    }

    @Test
    public void testDivision() {
        check("var x = 10; var result = x / 2 == 5.0;");
    }

    @Test
    public void testNoTop() {
        check("var x = 10; var result = !(x == 20);");
    }

    @Test
    public void testStringEquality() {
        check("var result = \"house\" == \"house\" && 'house' == 'house' && \"\" == 0;");
    }
    
    @Test
    public void testGlobalEscapeFunctionProperties() {
        eval("var global = Function(\"return this;\")()");
        eval("var desc = Object.getOwnPropertyDescriptor(global, 'escape');");
        assertThat(eval("desc.value != undefined")).isEqualTo(true);
        assertThat(eval("desc.value == global.escape")).isEqualTo(true);
        assertThat(eval("desc.writable")).isEqualTo(true);
        assertThat(eval("desc.configurable")).isEqualTo(true);
        assertThat(eval("desc.enumerable")).isEqualTo(true);
    }
    
    @Test
    public void testHexEscape() {
        // TODO: Better testing
        assertThat(eval("escape('Now is the winter')")).isEqualTo("Now%20is%20the%20winter");
    }
    
    @Test
    public void testGlobalUnescapeFunctionProperties() {
        eval("var global = Function(\"return this;\")()");
        eval("var desc = Object.getOwnPropertyDescriptor(global, 'unescape');");
        assertThat(eval("desc.value != undefined")).isEqualTo(true);
        assertThat(eval("desc.value == global.unescape")).isEqualTo(true);
        assertThat(eval("desc.writable")).isEqualTo(true);
        assertThat(eval("desc.configurable")).isEqualTo(true);
        assertThat(eval("desc.enumerable")).isEqualTo(true);
    }
    
}
