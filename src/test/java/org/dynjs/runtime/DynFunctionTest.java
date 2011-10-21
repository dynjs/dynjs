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
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynFunctionTest {

    private DynJSCompiler compiler;
    private Scope scope;
    private DynThreadContext context;

    @Before
    public void setup() {
        compiler = new DynJSCompiler();
        scope = new DynObject();
        context = new DynThreadContext();
    }

    @Test
    public void shouldReturnCorrectOffsets() {
        String d = "d";
        String a = "a";
        Object result1 = compiler.compile(new DynFunction() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .bipush(getArgumentOffset("d"))
                        .aaload()
                        .areturn();

            }

            @Override
            public String[] getArguments() {
                return new String[]{"a", "c", "d"};
            }
        }).call(context, scope, new Object[]{a, new DynObject(), d});
        assertThat(result1)
                .isNotNull()
                .isInstanceOf(String.class)
                .isEqualTo(d);
        Object result2 = compiler.compile(new DynFunction() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("c"))
                        .aaload()
                        .areturn();

            }

            @Override
            public String[] getArguments() {
                return new String[]{"a", "c", "d"};
            }
        }).call(context, scope, new Object[]{a, new DynObject(), d});
        assertThat(result2)
                .isNotNull()
                .isInstanceOf(DynObject.class);
    }

}
