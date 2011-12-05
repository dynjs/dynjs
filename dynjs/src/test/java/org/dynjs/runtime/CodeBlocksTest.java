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
import org.junit.Test;

public class CodeBlocksTest {

    private DynJSCompiler compiler = new DynJSCompiler();
    private Scope scope = new DynObject();
    private DynThreadContext context = new DynThreadContext();

    @Test
    public void testDynPropGet() {
        Function function = compiler.compile(new DynFunction() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlocks.GETPROP;
            }

            @Override
            public String[] getArguments() {
                return new String[]{"target", "name"};
            }
        });
        String propertyName = "constructor";
        DynObject object = new DynObject();
        Object result = function.call(context, new Object[]{object, propertyName});
    }

}
