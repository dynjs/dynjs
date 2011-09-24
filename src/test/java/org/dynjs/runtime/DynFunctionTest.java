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

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;
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
    }

    @Test
    public void shouldReturnCorrectOffsets() {
        DynString d = new DynString("d");
        DynString a = new DynString("a");
        DynAtom result1 = compiler.compile(new DynFunction(new String[]{"a", "c", "d"}) {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .bipush(getArgumentOffset("d"))
                        .aaload()
                        .areturn();

            }
        }).call(context, scope, new DynAtom[]{a, DynPrimitiveBoolean.TRUE, d});
        assertThat(result1)
                .isNotNull()
                .isInstanceOf(DynString.class)
                .isEqualTo(d);
        DynAtom result2 = compiler.compile(new DynFunction(new String[]{"a", "c", "d"}) {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("c"))
                        .aaload()
                        .areturn();

            }
        }).call(context, scope, new DynAtom[]{a, DynPrimitiveBoolean.TRUE, d});
        assertThat(result2)
                .isNotNull()
                .isInstanceOf(DynPrimitiveBoolean.class)
                .isEqualTo(DynPrimitiveBoolean.TRUE);
    }

}
