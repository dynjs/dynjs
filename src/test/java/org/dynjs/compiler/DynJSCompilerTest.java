/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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
package org.dynjs.compiler;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Scope;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.LabelNode;

import static me.qmx.jitescript.CodeBlock.*;

public class DynJSCompilerTest {

    private DynJSCompiler dynJSCompiler;
    private DynThreadContext context;
    private Scope scope;

    @Before
    public void setUp() throws Exception {
        final DynJSConfig cfg = new DynJSConfig();
        dynJSCompiler = new DynJSCompiler(cfg);
        context = new DynThreadContext();
        scope = new DynObject();
    }

    @Test
    public void testScriptCompiler() {
        dynJSCompiler.compile(new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                final LabelNode velse = new LabelNode();
                final LabelNode vout = new LabelNode();
                return new CodeBlock() {{
                    pushBoolean(true);
                    pushBoolean(true);
                    ifeq(velse);
                    ldc("then reached");
                    aprintln();
                    go_to(vout);
                    label(velse);
                    ldc("else reached");
                    label(vout);
                    aconst_null();
                }};
            }

            @Override public Position getPosition() {
                return null;
            }
        }).execute(new DynThreadContext());
    }

}
