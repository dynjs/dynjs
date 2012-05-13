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

public class DoWhileStatement extends BaseStatement implements Statement {

    private final Stack<LabelNode> labelStack;
    private final Statement vbool;
    private final BlockStatement vloop;

    public DoWhileStatement(Stack<LabelNode> labelStack, final Tree tree, final Statement vbool, final Statement vloop) {
        super(tree);
        this.labelStack = labelStack;
        this.vbool = vbool;
        this.vloop = (BlockStatement) vloop;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            labelStack.push(vloop.getBeginLabel());
            label(vloop.getBeginLabel());
            append(vloop.getCodeBlock());
            append(vbool.getCodeBlock());
            invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
            invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
            iffalse(vloop.getEndLabel());
            go_to(vloop.getBeginLabel());
            label(vloop.getEndLabel());
            labelStack.pop();
        }};
    }
}
