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
package org.dynjs.parser.ast;


import me.qmx.jitescript.CodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.*;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;

public class WithStatement extends AbstractCompilingStatement implements Statement {

    private final Expression expr;
    private final Statement block;

    public WithStatement(final Tree tree, BlockManager blockManager, Expression expr, Statement block) {
        super(tree, blockManager);
        this.expr = expr;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // context
                append( expr.getCodeBlock() );
                // context val
                append( jsGetValue() );
                // context val
                append( jsToObject() );
                // context obj
                append( CodeBlockUtils.compiledStatementBlock(getBlockManager(), "With", block) );
                // context obj block
                invokevirtual(p(ExecutionContext.class), "executeWith", sig(Completion.class, JSObject.class, BasicBlock.class));
                // completion
            }
        };
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("with (").append(this.expr.toString()).append(") {\n");
        buf.append( this.block.toIndentedString( indent + "  " ) );
        buf.append(indent).append("}");

        return buf.toString();
    }
}
