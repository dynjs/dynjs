/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
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

import org.dynjs.api.Function;
import org.dynjs.exception.ReferenceError;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSTest {

    private DynJS dynJS;
    private DynThreadContext context;
    private DynObject scope;

    @Before
    public void setUp() {
        dynJS = new DynJS();
        context = new DynThreadContext();
        scope = new DynObject();
        context.setScope(scope);
    }

    @Test
    public void assignsGlobalVariables() {
        dynJS.eval(context, "var x = 'test';");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(String.class);
//      TODO          .isEqualTo("test");
    }

    @Test
    public void defineUnInitializedGlobalVariables() {
        dynJS.eval(context, "var x;");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isEqualTo(DynThreadContext.UNDEFINED);
    }

    @Test
    public void assignsExprGlobalVariables() {
        dynJS.eval(context, "var x = 2 + 1;");
        Object atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(Double.class);

        assertThat(atom).isEqualTo(3.0);
    }

    @Test
    public void assignsExprMulGlobalVariables() {
        dynJS.eval(context, "var x = 2 * 3;");
        Object atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(Double.class);

        assertThat(atom).isEqualTo(6.0);
    }

    @Test
    public void assignsExprSubGlobalVariables() {
        dynJS.eval(context, "var x = 3 - 1;");
        Object atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(Double.class);

        assertThat(atom).isEqualTo(2.0);
    }

    @Test
    public void assignsComplexExprSubGlobalVariables() {
        dynJS.eval(context, "var x = 3 * 2 - 1;");
        Object atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(Double.class);

        assertThat(atom).isEqualTo(5.0);
    }

    @Test
    public void assignsComplexparExprSubGlobalVariables() {
        dynJS.eval(context, "var x = (3 * 2) - 1;");
        Object atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(Double.class);

        assertThat(atom).isEqualTo(5.0);
    }

    @Test
    public void assignsNamedEmptyFunction() {
        dynJS.eval(context, "function x(){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        dynJS.eval(context, "var x = function(a,b,c){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void buildFunctionWithBody() {
        dynJS.eval(context, "var x = function(a,b){var w = (1 + 2) * 3;}");
        Object actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, scope, new DynAtom[]{}))
                .isNull();
    }

    @Test
    public void buildFunctionWithMultipleStatementBody() {
        dynJS.eval(context, "var x = function(){var a = 1;var b = 2; var c = a + b;}");
        Object actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, scope, new DynAtom[]{}))
                .isNull();
    }

    @Test
    public void buildFunctionWithReturn() {
        dynJS.eval(context, "var x = function(){return 1+1;};");
        Object actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, scope, new DynAtom[]{}))
                .isNotNull()
                .isInstanceOf(Double.class)
                .isEqualTo(2.0);
    }

    //
    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        dynJS.eval(context, "print(x);");
    }

    @Test
    public void testIfStatement() {
        dynJS.eval(context, DynJSTest.class.getResourceAsStream("01_if_statement.js"));
        Object actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        Function function = (Function) actual;
        Object result = function.call(context, scope, new DynAtom[]{new DynObject()});
        assertThat(result)
                .isNotNull();
    }

    @Test
    public void testFunctionCall() {
        Object result = evalScript("02_function_call.js");
        assertThat(result)
                .isNotNull()
                .isInstanceOf(Double.class);
        assertThat(result).isEqualTo(1.0);

    }

    @Test
    public void testFactorial() {
        Object result = evalScript("03_factorial.js");
        assertThat(result)
                .isNotNull()
                .isInstanceOf(Double.class);
        assertThat(result).isEqualTo(120.0);
    }

    @Test
    public void testFibonnaci() {
        final Object result = evalScript("04_fib_recursive.js");
        assertThat(result).isEqualTo(8.0);
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
    }

    @Test
    public void testNullLiteral() {
        dynJS.eval(context, "var result = null");
        Object result = context.getScope().resolve("result");
        assertThat(result).isNull();
    }

    @Test
    public void testTernaryOperator() {
        dynJS.eval(context, "var result = 1 > 2 ? 55 : 56;");
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(56.0);
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

    private void check(String scriptlet) {
        check(scriptlet, true);
    }

    private void check(String scriptlet, Boolean expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }

    private Object evalScript(String scriptName) {
        dynJS.eval(context, DynJSTest.class.getResourceAsStream(scriptName));
        return scope.resolve("result");
    }
//
//    @Test
//    public void assignsObjectLiterals() {
//        dynJS.eval(context, scope, "var x = {lol:function(){}, name:'john doe'};");
//        assertThat(scope.resolve("x"))
//                .isNotNull()
//                .isInstanceOf(DynObject.class);
//        DynObject x = (DynObject) scope.resolve("x");
//        assertThat(x.resolve("lol"))
//                .isNotNull()
//                .isInstanceOf(Function.class);
//        assertThat(x.resolve("name"))
//                .isNotNull()
//                .isInstanceOf(DynString.class);
//
//    }
//
//    @Test
//    @Ignore("wip - qmx")
//    public void constructsNewObjectFromFunction() {
//        dynJS.eval(context, scope, "function MyObject(){}; var o = new MyObject();");
//        assertThat(scope.resolve("MyObject"))
//                .isNotNull()
//                .isInstanceOf(Function.class);
//        assertThat(scope.resolve("o"))
//                .isNotNull()
//                .isInstanceOf(DynObject.class);
//    }
//

}
