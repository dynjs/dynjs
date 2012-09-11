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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.LabelNode;

public class BlockStatement extends AbstractStatement implements Statement {

    private final List<Statement> blockContent;

    public BlockStatement(Tree tree, final List<Statement> blockContent) {
        super(tree);
        this.blockContent = blockContent;
    }

    public BlockStatement(final List<Statement> blockContent) {
        super(blockContent.isEmpty() ? null : blockContent.get(0).getPosition());
        this.blockContent = blockContent;
    }

    public List<Statement> getBlockContent() {
        return this.blockContent;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (this.blockContent == null) {
            return Collections.emptyList();
        }

        List<FunctionDeclaration> decls = new ArrayList<>();

        for (Statement each : this.blockContent) {
            if (each instanceof FunctionDeclaration) {
                decls.add((FunctionDeclaration) each);
            }
        }

        return decls;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        for (Statement each : this.blockContent) {
            if (each instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement statement = (VariableDeclarationStatement) each;
                decls.addAll(statement.getVariableDeclarations());
            }
        }
        return decls;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 12.1
                LabelNode abrupt = new LabelNode();
                LabelNode end = new LabelNode();

                append(normalCompletion());
                // completion
                astore(JSCompiler.Arities.COMPLETION);
                // <empty>

                for (Statement statement : blockContent) {
                    if (statement == null) {
                        continue;
                    }
                    LabelNode nonAbrupt = new LabelNode();
                    LabelNode bringForwardValue = new LabelNode();
                    LabelNode nextStatement = new LabelNode();

                    if (statement.getPosition() != null) {
                        line(statement.getPosition().getLine());
                        aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                        // context
                        ldc(statement.getPosition().getLine());
                        // context line
                        invokevirtual(p(ExecutionContext.class), "setLineNumber", sig(void.class, int.class));
                        // <empty>
                    }
                    append(statement.getCodeBlock());
                    // completion(cur)
                    dup();
                    // completion(cur) completion(cur)
                    append(handleCompletion(nonAbrupt, abrupt, abrupt, abrupt));

                    // ----------------------------------------
                    // Non-abrupt

                    label(nonAbrupt);
                    // completion(cur);
                    dup();
                    // completion(cur) completion(cur)
                    append(jsCompletionValue());
                    // completion(cur) value
                    ifnull(bringForwardValue);
                    // completion(cur)
                    astore(JSCompiler.Arities.COMPLETION);
                    // <empty>
                    go_to(nextStatement);

                    // ----------------------------------------

                    label(bringForwardValue);
                    // completion(cur)
                    dup();
                    // completion(cur) completion(cur)
                    aload(JSCompiler.Arities.COMPLETION);
                    // completion(cur) completion(cur) completion(prev)
                    append(jsCompletionValue());
                    // completion(cur) completion(cur) val(prev)
                    putfield(p(Completion.class), "value", ci(Object.class));
                    // completion(cur)
                    astore(JSCompiler.Arities.COMPLETION);
                    // <empty>
                    label(nextStatement);
                }

                go_to(end);

                // ----------------------------------------
                // ABRUPT

                label(abrupt);
                // completion(cur)
                astore(JSCompiler.Arities.COMPLETION);
                // <empty>

                // ----------------------------------------
                // END
                label(end);
                // <empty>
                aload(JSCompiler.Arities.COMPLETION);
            }
        };
    }

    public String dump(String indent) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(super.dump(indent));
        for (Statement each : this.blockContent) {
            buffer.append(each.dump(indent + "  "));
        }

        return buffer.toString();
    }

    public String toIndentedString(String indent) {
        StringBuffer buffer = new StringBuffer();

        for (Statement each : this.blockContent) {
            if (each != null) {
                buffer.append(each.toIndentedString(indent));
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }
}
