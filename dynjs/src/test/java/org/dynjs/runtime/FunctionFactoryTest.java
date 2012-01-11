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

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.compiler.DynJSCompiler;
import org.junit.Before;
import org.junit.Test;

import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.fest.assertions.Assertions.assertThat;

public class FunctionFactoryTest {

    private DynJS dynJS;
    private DynThreadContext context;

    @Before
    public void setUp() {
        dynJS = new DynJS();
        context = new DynThreadContext();
    }

    @Test
    public void testScopeSetting() {
        final CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .aload(0)
                .ldc("x")
                .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class))
                .areturn();
        final Function function = dynJS.compile(codeBlock, new String[]{});
        final Object x = new Object();
        function.define("x", x);
        assertThat(function.resolve("x")).isNotNull().isSameAs(x);
        assertThat(function.call(context, new Object[]{})).isSameAs(x);
    }

}
