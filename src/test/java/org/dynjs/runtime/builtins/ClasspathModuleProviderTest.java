/*
 * Copyright 2013 JBoss Inc
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

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class ClasspathModuleProviderTest extends AbstractDynJSTestSupport {
    
    @Test
    public void findsModulesOnTheClasspath() {
        eval("foo = require('foo')");
        assertThat(eval("foo")).isInstanceOf(JSFunction.class);
        assertThat(eval("foo()")).isEqualTo("bacon");
    }
    
    @Test
    public void allowsFileExtension() {
        eval("foo = require('foo.js')");
        assertThat(eval("foo")).isInstanceOf(JSFunction.class);
        assertThat(eval("foo()")).isEqualTo("bacon");
    }

    @Test
    public void testSupportsNestedRequires() {
        assertThat(eval("x = require('outer'); x.quadruple(4);")).isEqualTo(16L);
    }
    
    @Test
    public void supportsPackagedModules() {
        eval("foobar = require('foobar')");
        assertThat(eval("foobar")).isInstanceOf(JSFunction.class);
        assertThat(eval("foobar()")).isEqualTo("crunchy");
    }
    
    @Test(expected=ThrowException.class)
    public void throwsWhenModuleIsNotFound() {
        eval("x = require('does_not_exist');");
    }
    
    @Test(expected = ThrowException.class)
    public void testThrowsWithoutAnArgument() {
        eval("require();");
    }

    @Test
    public void testReturnsExportsWhenTheFileIsFound() {
        check("var result = require('my_module').message;", "Hello world");
    }

    @Test
    public void testHasGlobalBuiltinVisibility() {
        check("var result = require('my_module').myParseInt('55');", 65L);
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
    public void testSupportsModuleDotExportNotation() {
        eval("x = require('module_dot_export')");
        assertThat(eval("x.AnObject")).isNotEqualTo(Types.UNDEFINED);
        assertThat(eval("x.a_function()")).isEqualTo("hello!");
    }
    
    @Test
    public void testModuleId() {
        assertThat(eval("require('my_module').module_id")).isEqualTo("my_module.js");
    }
    
    @Test
    public void testCyclicDependencies() {
      eval("var a = require('a');");
      eval("var b = require('b');");
      assertThat(eval("a.a().b")).isEqualTo(eval("b.b"));
      assertThat(eval("b.b().a")).isEqualTo(eval("a.a"));
    }
}
