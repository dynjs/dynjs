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
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.objectweb.asm.tree.LabelNode;

public class IfStatement extends AbstractCompilingStatement implements Statement {

    private final Expression vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(final Tree tree, final BlockManager blockManager, final Expression vbool, final Statement vthen, final Statement velse) {
        super( tree, blockManager );
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode elseBranch = new LabelNode();
                LabelNode end = new LabelNode();

                append( vbool.getCodeBlock() );
                // value

                iffalse( elseBranch );
                // <empty>

                // ----------------------------------------
                // THEN

                append( CodeBlockUtils.invokeCompiledStatementBlock( getBlockManager(), "Then", vthen ) );
                // completion
                go_to( end );

                // ----------------------------------------
                // ELSE
                label( elseBranch );
                // <empty>

                append( CodeBlockUtils.invokeCompiledStatementBlock( getBlockManager(), "Else", velse ) );
                // completion

                label( end );
                // completion
                
                nop();
            }
        };
    }
}
