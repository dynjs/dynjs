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
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.junit.Before;
import org.junit.Test;

import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.fest.assertions.Assertions.assertThat;

public class DynalinkTest {

    private DynJS dynJS;
    private DynThreadContext context;

    @Before
    public void setUp() {
        dynJS = new DynJS();
        context = new DynThreadContext();
    }

    @Test
    public void testGetPropNonConstantName() {
        dynJS.eval(context, "var x = {w:function(){return 1;}};");
        final Object x = context.getScope().resolve("x");
        final CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .aload(0)
                .ldc("x")
                .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class))
                .ldc("w")
                .invokedynamic("dyn:getProp", sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .areturn();
        final Function fn = dynJS.compile(codeBlock, new String[]{});
        fn.define("x", x);
        final Object call = fn.call(context, new Object[]{});
        assertThat(call)
                .isNotNull()
                .isInstanceOf(Function.class);
    }

    @Test
    public void testSetPropNonConstantName() {
        dynJS.eval(context, "var x = {w:function(){return 1;}};");
        final Object x = context.getScope().resolve("x");
        final CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .aload(0)
                .ldc("x")
                .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class))
                .ldc("o")
                .ldc("any")
                .invokedynamic("dyn:setProp", sig(void.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .aconst_null()
                .areturn();
        final Function fn = dynJS.compile(codeBlock, new String[]{});
        fn.define("x", x);
        final Object call = fn.call(context, new Object[]{});

        assertThat(x)
                .isNotNull()
                .isInstanceOf(Scope.class);

        assertThat(((Scope) x).resolve("o")).isNotNull().isEqualTo("any");
    }

}
