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
import static me.qmx.jitescript.util.CodegenUtils.*;

import org.antlr.runtime.tree.Tree;
import org.dynjs.runtime.JSConstructor;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Types;

public class NewOperatorExpression extends AbstractExpression {

    private final Expression newExpr;

    public NewOperatorExpression(final Tree tree, final Expression newExpr) {
        super(tree);
        this.newExpr = newExpr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 11.2.2
                append(newExpr.getCodeBlock());
                // reference
                append(jsGetValue(JSFunction.class));
                // ctor-fn
                invokestatic(p(Types.class), "toConstructor", sig(JSConstructor.class, JSFunction.class));
                // ctor-fn
            }
        };
    }
}
