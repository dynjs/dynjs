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

import java.util.ArrayList;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.LabelNode;

public class IfStatement extends AbstractCompilingStatement implements Statement {

    private final Expression vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(final Tree tree, final BlockManager blockManager, final Expression vbool, final Statement vthen, final Statement velse) {
        super(tree, blockManager);
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }
    
    public Expression getTest() {
        return this.vbool;
    }
    
    public Statement getThenBlock() {
        return this.vthen;
    }
    
    public Statement getElseBlock() {
        return this.velse;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        decls.addAll( this.vthen.getVariableDeclarations() );
        if ( this.velse != null ) {
            decls.addAll( this.velse.getVariableDeclarations() );
        }
        return decls;
    }
    
    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode elseBranch = new LabelNode();
                LabelNode noElseBranch = new LabelNode();
                LabelNode end = new LabelNode();

                append(vbool.getCodeBlock());
                // value
                append( jsGetValue() );
                // value
                append(jsToBoolean());
                // Boolean
                invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
                // bool

                if (velse == null) {
                    // completion bool
                    iffalse(noElseBranch);
                } else {
                    iffalse(elseBranch);
                }
                // <empty>

                // ----------------------------------------
                // THEN

                append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Then", vthen));
                // completion
                go_to(end);

                // ----------------------------------------
                // ELSE
                if (velse == null) {
                    label(noElseBranch);
                    append(normalCompletion());
                } else {
                    label(elseBranch);
                    // <empty>
                    append(CodeBlockUtils.invokeCompiledStatementBlock(getBlockManager(), "Else", velse));
                    // completion
                }

                label(end);
                // completion

                nop();
            }
        };
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("if (").append(this.vbool.toString()).append(") {\n");
        buf.append(this.vthen.toIndentedString(indent + "  "));
        if (this.velse != null) {
            buf.append(indent).append("} else {\n").append(this.velse.toIndentedString(indent + "  "));
        }
        buf.append(indent).append("}");

        return buf.toString();
    }


    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
