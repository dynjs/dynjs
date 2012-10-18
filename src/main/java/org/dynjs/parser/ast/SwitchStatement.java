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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class SwitchStatement extends AbstractCompilingStatement {

    private Expression expr;
    private List<CaseClause> caseClauses;
    private DefaultCaseClause defaultClause;

    public SwitchStatement(final Tree tree, BlockManager blockManager, Expression expr, List<CaseClause> caseClauses) {
        super(tree, blockManager);
        this.expr = expr;
        this.caseClauses = caseClauses;
        Iterator<CaseClause> caseIter = caseClauses.iterator();

        while (caseIter.hasNext()) {
            CaseClause each = caseIter.next();
            if (each instanceof DefaultCaseClause) {
                this.defaultClause = (DefaultCaseClause) each;
                caseIter.remove();
            }
        }
    }
    
    public Expression getExpr() {
        return this.expr;
    }
    
    public List<CaseClause> getCaseClauses() {
        return this.caseClauses;
    }
    
    public DefaultCaseClause getDefaultCaseClause() {
        return this.defaultClause;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        for ( CaseClause each : caseClauses ) {
            decls.addAll( each.getVariableDeclarations() );
        }
        if ( this.defaultClause != null ) {
            decls.addAll( this.defaultClause.getVariableDeclarations() );
        }
        return decls;
    }
    
    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode end = new LabelNode();

                append(normalCompletion());
                // completion
                astore(JSCompiler.Arities.COMPLETION);
                // <empty>

                append(expr.getCodeBlock());
                // switchref
                append(jsGetValue());
                // switchval

                for (CaseClause eachCase : caseClauses) {
                    dup();
                    // switchval switchval
                    if (eachCase.getExpression() == null) {
                        pop();
                        // switchval
                        go_to(eachCase.getEntranceLabel());
                    } else {
                        aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                        // switchval switchval context
                        swap();
                        // switchval context switchval
                        append(eachCase.getExpression().getCodeBlock());
                        // switchval context switchval caseref
                        append(jsGetValue());
                        // switchval context switchval caseval
                        invokestatic(p(Types.class), "compareStrictEquality", sig(boolean.class, ExecutionContext.class, Object.class, Object.class));
                        // switchval bool
                        iftrue(eachCase.getEntranceLabel());
                        // switchval
                    }
                }

                // <empty>
                if (defaultClause != null) {
                    go_to(defaultClause.getEntranceLabel());
                } else {
                    // switchval
                    pop();
                    go_to(end);
                }

                int curCase = 0;

                while (curCase < caseClauses.size()) {
                    CaseClause eachCase = caseClauses.get(curCase);
                    CaseClause nextCase = null;
                    if (curCase + 1 < caseClauses.size()) {
                        nextCase = caseClauses.get(curCase + 1);
                    }

                    append(getCodeBlock(end, eachCase, nextCase, defaultClause));

                    ++curCase;
                }

                if (defaultClause != null) {
                    append(getCodeBlock(end, defaultClause, null, null));
                }

                label(end);
                // <empty>
                aload(JSCompiler.Arities.COMPLETION);
                // completion

            }
        };
    }

    protected CodeBlock getCodeBlock(final LabelNode end, final CaseClause curCase, final CaseClause nextCase, final CaseClause defaultCase) {
        return new CodeBlock() {
            {

                LabelNode normal = new LabelNode();
                LabelNode blockEnd = new LabelNode();
                LabelNode broke = new LabelNode();
                LabelNode abrupt = new LabelNode();

                label(curCase.getEntranceLabel());
                // switchval
                pop();
                label(curCase.getFallThroughLabel());
                // <empty>
                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Case", curCase.getBlock()));
                // completion
                dup();
                // completion completion
                append(handleCompletion(normal, broke, abrupt, abrupt));

                // ----------------------------------------
                // ----------------------------------------
                // NORMAL (fall-through)
                label(normal);
                // completion
                dup();
                // completion completion
                append(jsCompletionValue());
                // completion value
                ifnonnull(blockEnd);
                // completion

                // ----------------------------------------
                // BRING FORWARD (fall-through)
                dup();
                // completion completion
                aload(JSCompiler.Arities.COMPLETION);
                // completion completion completion(prev)
                append(jsCompletionValue());
                // completion completion value(prev)
                putfield(p(Completion.class), "value", ci(Object.class));
                // completion
                go_to(blockEnd);

                // ----------------------------------------
                // BREAK (fall-through)
                label(broke);
                // completion
                append(convertToNormal());
                // completion

                // ----------------------------------------
                // ABRUPT
                label(abrupt);
                // completion
                astore(JSCompiler.Arities.COMPLETION);
                // <empty>
                go_to(end);

                // ----------------------------------------
                // BLOCK END
                label(blockEnd);
                // completion
                astore(JSCompiler.Arities.COMPLETION);
                // <empty>

                if (nextCase != null) {
                    go_to(nextCase.getFallThroughLabel());
                }  else if ( defaultCase != null ) {
                    go_to(defaultCase.getFallThroughLabel());
                } else {
                    go_to( end );
                }
            }
        };

    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("switch (").append(expr.toString()).append(" ) {\n");
        for (CaseClause each : caseClauses) {
            buf.append(each.toIndentedString("  " + indent));
        }
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
