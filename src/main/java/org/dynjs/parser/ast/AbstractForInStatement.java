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

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractForInStatement extends AbstractCompilingStatement {

    private final Expression rhs;
    private final Statement block;

    public AbstractForInStatement(final Tree tree, final BlockManager blockManager, final Expression rhs, final Statement block) {
        super(tree, blockManager);
        this.rhs = rhs;
        this.block = block;
    }

    public abstract CodeBlock getFirstChunkCodeBlock();

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode nullObj = new LabelNode();
                LabelNode nextName = new LabelNode();
                LabelNode checkCompletion = new LabelNode();
                LabelNode bringForward = new LabelNode();
                LabelNode doBreak = new LabelNode();
                LabelNode end = new LabelNode();

                append(normalCompletion());
                // completion
                append(rhs.getCodeBlock());
                append(jsGetValue());
                // completion val
                dup();
                // completion val val
                getstatic(p(Types.class), "UNDEFINED", ci(Object.class));
                // completion val val UNDEF
                if_acmpeq(end);
                // completion val
                dup();
                // completion val val
                getstatic(p(Types.class), "NULL", ci(Object.class));
                // completion val val NULL
                if_acmpeq(nullObj);
                // completion val
                append(jsToObject());
                // completion jsObj

                // -----------------------------------------------
                // completion jsObj
                invokevirtual(p(JSObject.class), "getEnumerablePropertyNames", sig(NameEnumerator.class));
                // completion name-enum
                astore(4);
                // completion

                label(nextName);
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
                invokevirtual(p(Reference.class), "putValue", sig(void.class, Object.class));

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

                append(handleCompletion( /*normal*/nextName, /*break*/doBreak, /*continue*/nextName, /*return*/end) );
                // completion(cur)

                // -----------------------------------------------
                // NULL/UNDEF right-hand

                label(nullObj);
                // completion val
                pop();
                // completion
                go_to(end);

                // -----------------------------------------------
                label(doBreak);
                // completion(break)
                append(convertToNormal());
                // completion(normal);

                // -----------------------------------------------

                label(end);
                nop();

            }
        };
    }
}
