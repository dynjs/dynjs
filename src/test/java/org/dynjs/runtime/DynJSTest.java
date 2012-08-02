/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.fixtures.BypassFunction;
import org.dynjs.runtime.java.JavaRequireFunction;
import org.dynjs.runtime.java.SayHiToJava;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSTest extends AbstractDynJSTestSupport {

    @Override
    public DynJSConfig getConfig() {
        final DynJSConfig config = super.getConfig();
//        config.enableDebug();
        return config;
    }

    @Test
    public void evalLines() {
        getDynJS().evalLines(getContext(),
                "var x = 'test'",
                "var y = x");
        assertThat(getContext().getScope().resolve("y"))
                .isNotNull()
                .isInstanceOf(String.class)
                .isEqualTo("test");
    }

    @Test
    public void assignsGlobalVariables() {
        check("var result = 'test';", "test");
        check("var result = undefined; result = 1.0;", 1.0);
    }

    @Test
    public void defineUnInitializedGlobalVariables() {
        getDynJS().eval(getContext(), "var x;");
        assertThat(getContext().getScope().resolve("x"))
                .isNotNull()
                .isEqualTo(DynThreadContext.UNDEFINED);
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
        check("function x(){}; var result = x();", null);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        check("var x = function(a,b,c){}; var result = x();", null);
    }

    @Test
    public void buildFunctionWithBody() {
        check("var result = (function(a,b){var w = (1 + 2) * 3;})();", null);
    }

    @Test
    public void buildFunctionWithMultipleStatementBody() {
        check("var result = (function(){var a = 1;var b = 2; var c = a + b;})()", null);
    }

    @Test
    public void buildFunctionWithReturn() {
        check("var result = (function(){return 1+1;})() === 2;");
    }

    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        getDynJS().eval(getContext(), "print(x);");
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
    public void testEquality() {
        check("var result = 1 == 1;");
        check("var result = 1 != 2;");
    }

    @Test
    public void testMathOperations() {
        check("var x = 1 + 1;var result = x == 2");
        check("var x = 1 - 1;var result = x == 0");
        check("var x = 3 * 7;var result = x == 21");
        check("var x = 3 / 2;var result = x == 1.5");
        check("var x = 3 % 2;var result = x == 1");
    }

    @Test
    public void testHexValue() {
    	check("var result = 0x16", 22.0);
    }

    @Test
    public void testBitwise() {
    	check("var result = 0x000F & 0x2222", 2.0);
    	check("var result = 0x000F | 0x2222", 8751.0);
    	check("var result = 0x000F ^ 0x2222", 8749.0);
    	check("var result = 0x000F; result &= 0x2222", 2.0);
    	check("var result = 0x000F; result |= 0x2222", 8751.0);
    	check("var result = 0x000F; result ^= 0x2222", 8749.0);

    	check("var result = 9 << 2", 36.0);
    	check("var result = 9 >> 2", 2.0);
    	check("var result = -1 >>> 2", 1.073741823E9);
    	check("var result = 9; result <<= 2", 36.0);
    	check("var result = 9; result >>= 2", 2.0);
    	check("var result = -1; result >>>= 2", 1.073741823E9);
    }

    @Test
    public void testAssignments() {
        check("var x = 1;x += 1; var result = x == 2;");
        check("var x = 1;x -= 1; var result = x == 0;");
        check("var x = 1;x *= 3; var result = x == 3;");
        check("var x = 1;x /= 1; var result = x == 1;");
        check("var x = 2;x %= 1; var result = x == 0;");
    }

    @Test
    public void testLoop() {
        check("var x = 10;var y = 0; while(x < 10){x+=1;y+=1}; var result = y == 0;");
        check("var x = 10;var y = 0; do { x+=1;y+=1; } while(x < 10); var result = y == 1;");
    }

    @Test
    public void testFor() {
        check("var x = 0; for (var i =0;i < 10; i+=1){ x+=1;}; var result = x == 10");
        check("var x = 0; var i =0; for (var w = 0;i < 10; i+=1){ x+=1;}; var result = i == 10");
        check("var x = 0; for (; x < 10; x += 1) { x += 1; }; var result = x == 10");
        check("var i = 0; var x = 33; for(; i < 10; i++) { x -= 1} var result = x == 23");
    }

    @Test
    public void testContinue() {
        check("var x = 0; for (var i = 0;i < 10; i+=1){ continue; x+=1;}; var result = x == 0;");
        check("var x = 0; do { x+=1;if(x % 3 == 0) {continue;};x+=3 } while(x < 10); var result = x == 13;");
        check("var x = 0; while(x < 10) { x+=1; if(x % 2 == 0) {continue;};x+=3}; var result = x == 12;");
    }

    @Test
    public void testBreak() {
    	check("var x = 0; for (var i = 0;i < 10; i+=1){ x+=1; break;}; var result = x == 1.0;");
    	check("var x = 0; var i = 0; for (;i < 10; i+=1){ x+=1; break;}; var result = x == 1.0;");
    	check("var x = 0; do { x+=1;if(x % 3 == 0) {break;};x+=3 } while(x < 10); var result = x == 9;");
    	check("var x = 0; while(x < 10) { x+=1; if(x % 2 == 0) {break;};x+=3}; var result = x == 12;");
    }

    @Test
    public void testNullLiteral() {
        Object result = resultFor("var result = null");
        assertThat(result).isEqualTo(DynThreadContext.NULL);
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
                .isInstanceOf(DynObject.class);
    }

    @Test
    public void testBasicObjectLiteral() {
        final String expression = "var result = {w:true};";
        final Object result = resultFor(expression);
        assertThat(result)
                .isNotNull()
                .isInstanceOf(DynObject.class);
        assertThat(((DynObject) result).resolve("w")).isInstanceOf(Boolean.class).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void testObjectLiteralPropertyAccess() {
        check("var x = {w:true}; var result = x.w;", true);
        check("var x = {'y':false}; var result = x.y;", false);
        check("var x = {'z':true}; var result = x['z'];", true);
    }

    @Test
    public void testLiteralArray() {
        check("var x = [1,2,3]; var result = x[0] == 1", true);
        check("var x = [1]; x[0] = 2; var result = x[0] == 2", true);
        check("var x = [1,2]; x[0] = 4; x[1]= 3; var result = x[0] == 4 && x[1] == 3", true);
        check("var x = []; x[33] = 'lol'; var result = x[33] == 'lol';");
    }

    @Test
    public void testObjectAssignment() {
        check("var x = {}; x.a = 'lol'; var result = x.a == 'lol';");
    }

    @Test
    public void testBuiltinLoading() {
        getConfig().addBuiltin("sample", DynJSCompiler.wrapFunction(getContext(), new BypassFunction()));
        check("var result = sample(true);");
    }

    @Test
    public void testFunctionCall() {
        check("var result = (function (){return true;})();");
    }

    @Test
    public void testEval() {
        check("eval('var result = true');");
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
    @Ignore
    public void testJavaRequireFunctionLoading() {
        getConfig().addBuiltin("javaRequire", DynJSCompiler.wrapFunction(getContext(), new JavaRequireFunction()));
        getDynJS().eval(getContext(), "var NiceClass = javaRequire('org.dynjs.runtime.java.SayHiToJava');");
        getDynJS().eval(getContext(), "var x = new NiceClass");

        assertThat(getContext().getScope().resolve("NiceClass"))
                .isNotNull()
                .isInstanceOf(Class.class)
                .isEqualTo(SayHiToJava.class);

        assertThat(getContext().getScope().resolve("x"))
                .isInstanceOf(SayHiToJava.class);

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
    public void tryCatchBlock() {
        Object result = resultFor("var y = {lol:'zomg'}; try { throw 'mud'; } catch (e) { y.e = e; }; var result = y.e;");
        assertThat(result).isInstanceOf(DynJSException.class);
    }

    @Test
    public void tryCatchFinallyBlock() {
        check("var result = undefined; try { throw 'mud'; } catch (e) { result = 'wrong'; } finally { result = 'lol';};", "lol");
    }

    @Test
    public void tryFinallyBlock() {
        check("var result = undefined; try { throw 'mud'; } finally { result = 'mud'; };", "mud");
    }

    @Test
    public void testDeleteOper() {
        check("var x = {a:'lol'}; var result = delete x.a;", false);
    }

    @Test
    public void testUnaryMinusOper() {
    	check("var result = -1 + 1", 0.0);
    	check("var result = -1", -1.0);
    	check("var x = 1; var result = -x == -1");
    }
}
