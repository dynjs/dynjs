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

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class LogicalNotOperatorExpression extends AbstractExpression {

    private final Expression expr;

    public LogicalNotOperatorExpression(final Tree tree, final Expression expr) {
        super(tree);
        this.expr = expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode returnFalse = new LabelNode();
                LabelNode end = new LabelNode();

                append(expr.getCodeBlock());
                // obj
                append(jsGetValue());
                // val
                append(jsToBoolean());
                // Boolean
                invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
                // bool
                iftrue(returnFalse);
                iconst_1();
                go_to(end);
                label(returnFalse);
                iconst_0();
                label(end);
                invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                nop();
            }
        };
    }

}