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

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.Reference;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractForInStatement extends AbstractIteratingStatement {

    private final Expression rhs;
    private final Statement block;

    public AbstractForInStatement(final Tree tree, final BlockManager blockManager, final Expression rhs, final Statement block) {
        super(tree, blockManager);
        this.rhs = rhs;
        this.block = block;
    }

    public Expression getRhs() {
        return this.rhs;
    }

    public Statement getBlock() {
        return this.block;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }
    
    public abstract CodeBlock getFirstChunkCodeBlock();

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode nextName = new LabelNode();
                LabelNode checkCompletion = new LabelNode();
                LabelNode bringForward = new LabelNode();
                LabelNode doBreak = new LabelNode();
                LabelNode doContinue = new LabelNode();
                LabelNode undefEnd = new LabelNode();
                LabelNode end = new LabelNode();

                append(normalCompletion());
                // completion
                append(rhs.getCodeBlock());
                // completion val
                append(jsGetValue());
                // completion val
                dup();
                // completion val val
                append(jsPushUndefined());
                // completion val val UNDEF
                if_acmpeq(undefEnd);
                // completion val
                dup();
                // completion val val
                append(jsPushNull());
                // completion val val NULL
                if_acmpeq(undefEnd);
                // completion val
                append(jsToObject());
                // completion jsObj

                // -----------------------------------------------
                // completion jsObj
                invokeinterface(p(JSObject.class), "getAllEnumerablePropertyNames", sig(NameEnumerator.class));
                // completion name-enum
                astore(4);
                // completion
                label(nextName);
                // completion
                aload(4);
                // completion name-enum
                invokevirtual(p(NameEnumerator.class), "hasNext", sig(boolean.class));
                // completion bool
                iffalse(end);
                // completion 
                aload(4);
                // completion name-enum
                invokevirtual(p(NameEnumerator.class), "next", sig(String.class));
                // completion str

                append(getFirstChunkCodeBlock());
                // completion str ref
                swap();
                // completion ref str
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // completion ref str context
                swap();
                // completion ref context str
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // completion 
                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "For", block));
                // completion(prev) completion(cur)
                dup();
                // completion(prev) completion(cur) completion(cur)
                append(jsCompletionValue());
                // completion(prev) completion(cur) val(cur)
                ifnull(bringForward);
                // completion(prev) completion(cur) 

                // ----------------------------------
                // has value
                swap();
                // completion(cur) completion(prev) 
                pop();
                // completion(cur)
                go_to(checkCompletion);

                // ----------------------------------------
                // bring previous value forward

                label(bringForward);
                // completion(prev) completion(cur) 
                dup_x1();
                // completion(cur) completion(prev) completion(cur) 
                swap();
                // completion(cur) completion(cur) completion(prev)
                append(jsGetValue());
                // completion(cur) completion(cur) val(prev)
                putfield(p(Completion.class), "value", ci(Object.class));
                // completion(cur) 

                // -----------------------------------------------
                label(checkCompletion);
                // completion(cur) 
                dup();
                // completion(cur) completion(cur)
                append(handleCompletion(nextName, doBreak, doContinue, end));
                // completion

                // ----------------------------------------
                // BREAK
                label(doBreak);
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
                
                label( doContinue );
                // completion(block,CONTINUE)
                dup();
                // completion completion
                append( jsCompletionTarget() );
                // completion target
                append( isInLabelSet() );
                // completion bool
                iffalse(end);
                // completion
                go_to( nextName );

                // -----------------------------------------------
                // RHS is undefined
                // completion undef
                label(undefEnd);
                // completion undef
                pop();
                // completion

                // -----------------------------------------------

                label(end);
                // completion
                nop();

            }
        };
    }
}
