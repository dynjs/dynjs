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
import org.dynjs.runtime.BlockManager;

public class ForVarDeclStatement extends AbstractForStatement {

    private final VariableDeclarationStatement declList;

    public ForVarDeclStatement(final Tree tree, final BlockManager blockManager, final VariableDeclarationStatement declList, final Expression test,
            final Expression increment, final Statement block) {
        super(tree, blockManager, test, increment, block);
        this.declList = declList;
    }

    @Override
    public CodeBlock getFirstChunkCodeBlock() {
        return new CodeBlock() {
            {
                for (VariableDeclaration decl : declList.getVariableDeclarations()) {
                    append(decl.getCodeBlock());
                    pop();
                }
            }
        };
    }

}
