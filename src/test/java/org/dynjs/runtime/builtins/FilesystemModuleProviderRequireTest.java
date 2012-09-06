/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.junit.Before;
import org.junit.Test;

public class FilesystemModuleProviderRequireTest extends AbstractDynJSTestSupport {

    @Before
    public void setUpLater() {
        getContext().getGlobalObject().addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
    }

    @Test(expected = ThrowException.class)
    public void testThrowsWithoutAnArgument() {
        eval("require();");
    }

    @Test(expected = ThrowException.class)
    public void testThrowsWhenTheFileIsNotFound() {
        eval("require('nonexistant_module');");
    }

    @Test
    public void testReturnsExportsWhenTheFileIsFound() {
        check("var result = require('my_module').message;", "Hello world");
    }

    @Test
    public void testAllowsFileExtension() {
        assertThat(eval("require('my_module.js').message;")).isEqualTo("Hello world");
    }

    @Test
    public void testFindsPackagedModules() {
        assertThat(eval("require('amodule').message")).isEqualTo("Hello world");
    }

    @Test
    public void testExportsFunctions() {
        assertThat(eval("require('my_module').sayHello();")).isEqualTo("Hello again");
    }

    @Test
    public void testAllowsSettingExportsToAnArbitraryThing() {
        assertThat(eval("require('my_export_thingy').doesSomething();")).isEqualTo("A thingy!");
    }

    @Test
    public void testHasPrivateFunctions() {
        assertThat(eval("require('my_module').farewell();")).isEqualTo("Goodbye, cruel world.");
        assertThat(eval("require('my_module').sayGoodbye")).isEqualTo(Types.UNDEFINED);
    }

    @Test
    public void testKeepsPrivateVariablesPrivate() {
        assertThat(eval("require('my_module').privateVariable")).isEqualTo(Types.UNDEFINED);
    }

    @Test
    public void testSupportsNestedRequires() {
        assertThat(eval("x = require('outer'); x.quadruple(4);")).isEqualTo(16L);
    }

    /*
    @Test
    public void testJavaImplementedRequires() {
        check("var x = require('java_impl'); var result = x.cheese();", "cheddar");
    }
     */

}
