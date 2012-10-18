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

public class WhileStatement extends AbstractIteratingStatement {

    private final Expression vbool;
    private final Statement vloop;

    public WhileStatement(final Tree tree, BlockManager blockManager, Expression vbool, Statement vloop) {
        super(tree, blockManager);
        this.vbool = vbool;
        this.vloop = vloop;
    }
    
    public Expression getTest() {
        return this.vbool;
    }
    
    public Statement getBlock() {
        return this.vloop;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.vloop.getVariableDeclarations();
    }
    
    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode end = new LabelNode();
                LabelNode breakTarget = new LabelNode();
                LabelNode continueTarget = new LabelNode();
                LabelNode begin = new LabelNode();

                append(normalCompletion());
                // completion(block)

                label(begin);
                append(vbool.getCodeBlock());
                // completion(block) result
                append(jsGetValue());
                // completion(block) result
                append(jsToBoolean());
                // completion(block) Boolean
                invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
                // completion(block) bool

                iffalse(end);
                // completion(block)

                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Do", vloop));
                // completion(block,prev) completion(block,cur)
                swap();
                // completion(block,cur) completion(block,prev)
                pop();
                // completion(block,cur)

                dup();
                // completion(block) completion(block)
                append(handleCompletion(begin, breakTarget, continueTarget, end));

                // completion(block)

                // ----------------------------------------
                // BREAK
                label(breakTarget);
                // completion(block,BREAK)
                dup();
                // completion completion
                append( jsCompletionTarget() );
                // completion target
                append( isInLabelSet() );
                // completion bool
                iffalse(end);
                // completion
                append(convertToNormal());
                // completion(block,NORMAL)
                go_to( end );
                
                // ----------------------------------------
                // CONTINUE
                
                label( continueTarget );
                // completion(block,CONTINUE)
                dup();
                // completion completion
                append( jsCompletionTarget() );
                // completion target
                append( isInLabelSet() );
                // completion bool
                iffalse(end);
                // completion
                go_to( begin );
                
                // ----------------------------------------
                label(end);
                // completion(block)
                nop();
            }
        };
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("while (").append(this.vbool.toString()).append(") {\n");
        buf.append(this.vloop.toIndentedString(indent + "  "));
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
