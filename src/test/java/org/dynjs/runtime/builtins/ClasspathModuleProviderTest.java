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
}
