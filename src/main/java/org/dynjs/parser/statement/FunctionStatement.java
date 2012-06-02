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

import java.util.List;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class FunctionStatement extends BaseStatement implements Statement {

    private final DynThreadContext context;
    private final String identifier;
    private final List<String> args;
    private final Statement block;

    public FunctionStatement(final Tree tree, final DynThreadContext context, final List<String> args, final Statement block) {
        this(tree, context, null, args, block);
    }

    public FunctionStatement(final Tree tree, final DynThreadContext context, final String identifier, final List<String> args, final Statement block) {
        super(tree);
        this.context = context;
        this.identifier = identifier;
        this.args = args;
        this.block = block != null ? block : new EmptyStatement();
    }

    @Override
    public CodeBlock getCodeBlock() {
        final Integer slot = context.store(block.getCodeBlock());
        // put arguments on stack
        CodeBlock codeBlock = new CodeBlock(3);

        codeBlock = retrieveNewStringArrayReference(codeBlock);

        for (int i = 0; i < args.size(); i++) {
            codeBlock = storeArrayReference(i, codeBlock);
        }

        codeBlock = getRuntime(codeBlock).aload(1);
        codeBlock = retrieveFromSlot(slot, codeBlock);
        codeBlock = retrieveCompiledArrayBlock(codeBlock);

        if (identifier != null) {
            // TODO DRY
            codeBlock = retrievePropertyReference(codeBlock);
        }

        return codeBlock;
    }

    private CodeBlock retrievePropertyReference(final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            astore(3);
            aload(DynJSCompiler.Arities.THIS);
            ldc(identifier);
            aload(3);
            invokedynamic("dyn:setProp", sig(void.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        }};
    }

    private CodeBlock storeArrayReference(final int stackReference, final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            aload(4);
            bipush(stackReference);
            ldc(args.get(stackReference));
            aastore();
        }};
    }

    private CodeBlock retrieveCompiledArrayBlock(final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            aload(4);
            invokevirtual(DynJSCompiler.Types.RUNTIME, "compile", sig(Object.class, DynThreadContext.class, CodeBlock.class, String[].class));
        }};
    }

    private CodeBlock retrieveNewStringArrayReference(final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            bipush(args.size());
            anewarray(p(String.class));
            astore(4);
        }};
    }

    private CodeBlock retrieveFromSlot(final Integer slot, final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            aload(DynJSCompiler.Arities.CONTEXT);
            bipush(slot);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "retrieve", sig(CodeBlock.class, int.class));
        }};
    }

    private CodeBlock getRuntime(final CodeBlock codeBlock) {
        return new CodeBlock() {{
            append(codeBlock);
            aload(DynJSCompiler.Arities.CONTEXT);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "getRuntime", sig(DynJS.class));
        }};
    }

}
