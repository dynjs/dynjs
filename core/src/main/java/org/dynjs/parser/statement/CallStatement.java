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
package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class CallStatement implements Statement {

    private final DynThreadContext context;
    private final Statement lhs;
    private final List<Statement> args;

    public CallStatement(DynThreadContext context, Statement lhs, List<Statement> args) {
        this.context = context;
        this.lhs = lhs;
        this.args = args;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = newCodeBlock();
        codeBlock = codeBlock
                .bipush(args.size())
                .anewarray(p(Object.class))
                .astore(4);

        for (int i = 0; i < args.size(); i++) {
            codeBlock = codeBlock
                    .aload(4)
                    .bipush(i)
                    .append(args.get(i).getCodeBlock())
                    .aastore();
        }

        codeBlock = codeBlock
                .append(lhs.getCodeBlock())
                .aload(DynJSCompiler.Arities.CONTEXT)
                .aload(4)
                .invokedynamic("dynjs:runtime:call", sig(Object.class, Function.class, DynThreadContext.class, Object[].class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);

        return codeBlock;
    }
}
