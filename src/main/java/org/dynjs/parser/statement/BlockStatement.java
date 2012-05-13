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
import org.dynjs.parser.Statement;
import org.objectweb.asm.tree.LabelNode;

import java.util.List;

public class BlockStatement extends BaseStatement implements Statement {

    private LabelNode beginLabel = new LabelNode();
    private LabelNode endLabel = new LabelNode();
    private final List<Statement> blockContent;

    public BlockStatement(final Tree tree, final List<Statement> blockContent) {
        super(tree);
        this.blockContent = blockContent;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            for (Statement statement : blockContent) {
                append(statement.getCodeBlock());
            }
        }};
    }

    public LabelNode getBeginLabel() {
        return beginLabel;
    }

    public LabelNode getEndLabel() {
        return endLabel;
    }
}
