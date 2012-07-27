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
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

import java.util.Stack;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ForStepExprStatement extends BaseStatement implements Statement {

    private final Stack<LabelNode> labelStack;
    private final Stack<LabelNode> breakStack;
    private final Statement initialize;
    private final Statement test;
    private final Statement increment;
    private final BlockStatement statement;
    private final LabelNode preIncrement = new LabelNode();

    public ForStepExprStatement(Stack<LabelNode> labelStack, Stack<LabelNode> breakStack, final Tree tree, final Statement initialize, final Statement test, final Statement increment, final Statement statement) {
        super(tree);
        this.labelStack = labelStack;
		this.breakStack = breakStack;
        this.initialize = initialize;
        this.test = test;
        this.increment = increment;
        this.statement = (BlockStatement) statement;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            labelStack.push(preIncrement);
            breakStack.push(statement.getEndLabel());
            if (initialize != null) {
            	append(initialize.getCodeBlock());
            }
            label(statement.getBeginLabel());
            append(test.getCodeBlock());
            invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
            invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
            iffalse(statement.getEndLabel());
            append(statement.getCodeBlock());
            label(preIncrement);
            append(increment.getCodeBlock());
            if (increment instanceof AbstractUnaryOperationStatement) {
            	pop();
            }
            go_to(statement.getBeginLabel());
            label(statement.getEndLabel());
            labelStack.pop();
            breakStack.pop();
        }};
    }
}
