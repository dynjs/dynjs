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
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

import static me.qmx.jitescript.CodeBlock.*;
import static me.qmx.jitescript.util.CodegenUtils.*;

public class IfStatement extends BaseStatement implements Statement {

    private final DynThreadContext context;
    private final Statement vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(final Tree tree, final DynThreadContext context, final Statement vbool, final Statement vthen, final Statement velse) {
        super(tree);
        this.context = context;
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        LabelNode elseBlock = new LabelNode();
        LabelNode outBlock = new LabelNode();
        CodeBlock elseCodeBlock = velse != null ? velse.getCodeBlock() : newCodeBlock();
        CodeBlock codeBlock = newCodeBlock()
                .append(vbool.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(elseBlock)
                .append(vthen.getCodeBlock())
                .go_to(outBlock)
                .label(elseBlock)
                .append(elseCodeBlock)
                .label(outBlock);
        return codeBlock;
    }
}
