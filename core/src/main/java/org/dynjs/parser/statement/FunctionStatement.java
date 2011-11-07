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
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class FunctionStatement implements Statement {

    private final DynThreadContext context;
    private final String identifier;
    private final List<String> args;
    private final Statement block;

    public FunctionStatement(final DynThreadContext context, final List<String> args, final Statement block) {
        this(context, null, args, block);
    }

    public FunctionStatement(final DynThreadContext context, final String identifier, final List<String> args, final Statement block) {
        this.context = context;
        this.identifier = identifier;
        this.args = args;
        this.block = block != null ? block : new EmptyStatement();
    }

    @Override
    public CodeBlock getCodeBlock() {
        final Integer slot = context.store(block.getCodeBlock());
        // put arguments on stack
        CodeBlock codeBlock = newCodeBlock(3);

        codeBlock = codeBlock
                .bipush(args.size())
                .anewarray(p(String.class))
                .astore(4);

        for (int i = 0; i < args.size(); i++) {
            codeBlock = codeBlock
                    .aload(4)
                    .bipush(i)
                    .ldc(args.get(i))
                    .aastore();
        }

        codeBlock = getRuntime(codeBlock);
        codeBlock = retrieveFromSlot(slot, codeBlock);
        codeBlock = codeBlock
                .aload(4)
                .invokevirtual(DynJSCompiler.Types.RUNTIME, "compile", sig(Function.class, CodeBlock.class, String[].class));

        if (identifier != null) {
            // TODO DRY
            codeBlock = codeBlock
                    .astore(3)
                    .aload(DynJSCompiler.Arities.THIS)
                    .ldc(identifier)
                    .aload(3)
                    .invokeinterface(DynJSCompiler.Types.Scope, "define", sig(void.class, String.class, Object.class));
        }

        return codeBlock;
    }

    private CodeBlock retrieveFromSlot(Integer slot, CodeBlock codeBlock) {
        codeBlock = codeBlock
                .aload(DynJSCompiler.Arities.CONTEXT)
                .bipush(slot)
                .invokevirtual(DynJSCompiler.Types.CONTEXT, "retrieve", sig(CodeBlock.class, int.class));
        return codeBlock;
    }

    private CodeBlock getRuntime(CodeBlock codeBlock) {
        codeBlock = codeBlock
                .aload(DynJSCompiler.Arities.CONTEXT)
                .invokevirtual(DynJSCompiler.Types.CONTEXT, "getRuntime", sig(DynJS.class));
        return codeBlock;
    }

}
