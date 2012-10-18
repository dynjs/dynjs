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

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.LabelNode;

public class DoWhileStatement extends AbstractIteratingStatement {

    private final Expression test;
    private final Statement block;

    public DoWhileStatement(final Tree tree, BlockManager blockManager, final Expression test, final Statement block) {
        super(tree, blockManager);
        this.test = test;
        this.block = block;
    }
    
    public Expression getTest() {
        return this.test;
    }
    
    public Statement getBlock() {
        return this.block;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode begin = new LabelNode();
                LabelNode normalTarget = new LabelNode();
                LabelNode breakTarget = new LabelNode();
                LabelNode continueTarget = new LabelNode();
                LabelNode end = new LabelNode();

                label(begin);
                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Do", block));
                // completion(block)
                dup();
                // completion(block) completion(block)
                append(handleCompletion(normalTarget, breakTarget, continueTarget, end));

                // ----------------------------------------
                // NORMAL
                label(normalTarget);
                // completion(block)

                append(test.getCodeBlock());
                // completion(block) result
                append(jsGetValue());
                // completion(block) result
                append(jsToBoolean());
                // completion(block) Boolean
                invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
                // completion(block) bool
                iffalse(end);
                pop();
                // <EMPTY>
                go_to(begin);

                // ----------------------------------------
                // BREAK
                label(breakTarget);
                // completion(block,BREAK)
                dup();
                // completion completion
                append(jsCompletionTarget());
                // completion target
                append(isInLabelSet());
                // completion bool
                iffalse(end);
                // completion
                append(convertToNormal());
                // completion(block,NORMAL)
                go_to(end);

                // ----------------------------------------
                // CONTINUE

                label(continueTarget);
                // completion(block,CONTINUE)
                dup();
                // completion completion
                append(jsCompletionTarget());
                // completion target
                append(isInLabelSet());
                // completion bool
                iffalse(end);
                // completion
                go_to(normalTarget);

                // ----------------------------------------
                label(end);
                // completion(block)
                nop();
                // completion(block)
            }
        };
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("do {\n");
        buf.append(indent).append("} while (").append(test.toString()).append(")");
        return buf.toString();
    }
}
