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

    @Before
    public void setUp() {
        dynJS = new DynJS();
        context = new DynThreadContext();
    }

    @Test
    public void assignsGlobalVariables() {
        dynJS.eval(context, "var x = 'test';");
        assertThat(context.getScope().resolve("x"))
                .isNotNull()
                .isInstanceOf(String.class);
//      TODO          .isEqualTo("test");
    }

    @Test
    public void defineUnInitializedGlobalVariables() {
        dynJS.eval(context, "var x;");
        assertThat(context.getScope().resolve("x"))
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
        dynJS.eval(context, "function x(){};");
        assertThat(context.getScope().resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        dynJS.eval(context, "var x = function(a,b,c){};");
        assertThat(context.getScope().resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void buildFunctionWithBody() {
        dynJS.eval(context, "var x = function(a,b){var w = (1 + 2) * 3;}");
        Object actual = context.getScope().resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, new Object[]{}))
                .isNull();
    }

    @Test
    public void buildFunctionWithMultipleStatementBody() {
        dynJS.eval(context, "var x = function(){var a = 1;var b = 2; var c = a + b;}");
        Object actual = context.getScope().resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, new Object[]{}))
                .isNull();
    }

    @Test
    public void buildFunctionWithReturn() {
        dynJS.eval(context, "var x = function(){return 1+1;};");
        Object actual = context.getScope().resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function) actual).call(context, new Object[]{}))
                .isNotNull()
                .isInstanceOf(Double.class)
                .isEqualTo(2.0);
    }

    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        dynJS.eval(context, "print(x);");
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
        assertThat(context.getScope().resolve("result")).isNull();
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

    private void check(String scriptlet) {
        check(scriptlet, true);
    }

    private void check(String scriptlet, Boolean expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }

}
