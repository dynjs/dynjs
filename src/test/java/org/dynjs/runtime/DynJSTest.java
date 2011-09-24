/**
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
import org.dynjs.runtime.primitives.DynPrimitiveUndefined;
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
    }

    @Test
    public void assignsGlobalVariables() {
        dynJS.eval(context, scope, "var x = 'test';");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(DynString.class);
    }

    @Test
    public void defineUnInitializedGlobalVariables() {
        dynJS.eval(context, scope, "var x;");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isEqualTo(DynPrimitiveUndefined.UNDEFINED);
    }

    @Test
    public void assignsExprGlobalVariables() {
        dynJS.eval(context, scope, "var x = 2 + 1;");
        DynAtom atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(DynNumber.class);

        assertThat(((DynNumber) atom).getValue()).isEqualTo(3.0);
    }

    @Test
    public void assignsExprMulGlobalVariables() {
        dynJS.eval(context, scope, "var x = 2 * 3;");
        DynAtom atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(DynNumber.class);

        assertThat(((DynNumber) atom).getValue()).isEqualTo(6.0);
    }

    @Test
    public void assignsExprSubGlobalVariables() {
        dynJS.eval(context, scope, "var x = 3 - 1;");
        DynAtom atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(DynNumber.class);

        assertThat(((DynNumber) atom).getValue()).isEqualTo(2.0);
    }

    @Test
    public void assignsComplexExprSubGlobalVariables() {
        dynJS.eval(context, scope, "var x = 3 * 2 - 1;");
        DynAtom atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(DynNumber.class);

        assertThat(((DynNumber) atom).getValue()).isEqualTo(5.0);
    }

    @Test
    public void assignsComplexparExprSubGlobalVariables() {
        dynJS.eval(context, scope, "var x = (3 * 2) - 1;");
        DynAtom atom = scope.resolve("x");
        assertThat(atom)
                .isNotNull()
                .isInstanceOf(DynNumber.class);

        assertThat(((DynNumber) atom).getValue()).isEqualTo(5.0);
    }

    @Test
    public void assignsNamedEmptyFunction() {
        dynJS.eval(context, scope, "function x(){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        dynJS.eval(context, scope, "var x = function(a,b,c){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void buildFunctionWithBody(){
        dynJS.eval(context, scope, "var x = function(a,b){var w = (1 + 2) * 3;}");
        DynAtom actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function)actual).call(context, scope, new DynAtom[]{}))
                .isNull();
    }

    @Test
    public void buildFunctionWithMultipleStatementBody(){
        dynJS.eval(context, scope, "var x = function(){var a = 1;var b = 2; var c = a + b;}");
        DynAtom actual = scope.resolve("x");
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Function.class);

        assertThat(((Function)actual).call(context, scope, new DynAtom[]{}))
                .isNull();
    }

    //
    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        dynJS.eval(context, scope, "print(x);");
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
