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
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public class ResolveByIndexStatement extends BaseStatement implements Statement {

    private final Statement lhs;
    private final Statement index;

    public ResolveByIndexStatement(final Tree tree, final Statement lhs, final Tree treeIndex, final String index) {
        this(tree, lhs, new StringLiteralStatement(treeIndex, index));

    }

    public ResolveByIndexStatement(final Tree tree, final Statement lhs, final Statement index) {
        super(tree);
        this.lhs = lhs;
        this.index = index;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = new CodeBlock() {{
            append(lhs.getCodeBlock());
            append(index.getCodeBlock());
        }};
        if (index instanceof NumberLiteralStatement) {
            return codeBlock.invokedynamic("dyn:getElement", DynJSCompiler.Signatures.ARITY_2, RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        } else {
            return codeBlock.invokedynamic("dyn:getProp", DynJSCompiler.Signatures.ARITY_2, RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        }
    }

    public Statement getLhs() {
        return lhs;
    }

    public Statement getIndex() {
        return index;
    }
}
