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
import org.junit.Before;
import org.junit.Ignore;
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
    public void assignsNamedEmptyFunction() {
        dynJS.eval(context, scope, "function x(){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void assignsAnonymousEmptyFunction() {
        dynJS.eval(context, scope, "var x = function(){};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorWhenCallAnonExistingReference() {
        dynJS.eval(context, scope, "print(x);");
    }

    @Test
    public void assignsObjectLiterals() {
        dynJS.eval(context, scope, "var x = {lol:function(){}, name:'john doe'};");
        assertThat(scope.resolve("x"))
                .isNotNull()
                .isInstanceOf(DynObject.class);
        DynObject x = (DynObject) scope.resolve("x");
        assertThat(x.resolve("lol"))
                .isNotNull()
                .isInstanceOf(Function.class);
        assertThat(x.resolve("name"))
                .isNotNull()
                .isInstanceOf(DynString.class);

    }
}
