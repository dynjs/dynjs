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
package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class CallStatement extends BaseStatement implements Statement {

    private final DynThreadContext context;
    private final Statement lhs;
    private final List<Statement> args;

    public CallStatement(final Tree tree, final DynThreadContext context, final Statement lhs, final List<Statement> args) {
        super(tree);
        this.context = context;
        this.lhs = lhs;
        this.args = args;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            bipush(args.size());
            anewarray(p(Object.class));
            astore(4);

            for (int i = 0; i < args.size(); i++) {
                aload(4);
                bipush(i);
                append(args.get(i).getCodeBlock());
                aastore();
            }

            append(CodeBlockUtils.offset( lhs.getCodeBlock(), 1) );
            aload(DynJSCompiler.Arities.CONTEXT);
            aload(4);
            invokedynamic("call2", sig(Object.class, Object.class, DynThreadContext.class, Object[].class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
        }};
    }
}
