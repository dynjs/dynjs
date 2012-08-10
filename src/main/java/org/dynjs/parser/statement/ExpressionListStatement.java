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

import java.util.ArrayList;
import java.util.List;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;

public class ExpressionListStatement extends AbstractStatement implements Statement {

    private final List<Statement> exprList;

    public ExpressionListStatement(final Tree tree, final List<Statement> exprList) {
        super(tree);
        this.exprList = new ArrayList<Statement>(exprList);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            for (Statement statement : exprList) {
                append(statement.getCodeBlock());
            }
        }};
    }
}
