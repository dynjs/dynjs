/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
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

import me.qmx.internal.org.objectweb.asm.tree.LabelNode;
import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class IfStatement implements Statement {

    private final DynThreadContext context;
    private final Statement vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(DynThreadContext context, Statement vbool, Statement vthen, Statement velse) {
        this.context = context;
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        LabelNode elseBlock = new LabelNode();
        LabelNode outBlock = new LabelNode();
        CodeBlock codeBlock = newCodeBlock()
                .append(vbool.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(elseBlock)
                .append(vthen.getCodeBlock())
                .go_to(outBlock)
                .label(elseBlock)
                .append(velse.getCodeBlock())
                .label(outBlock);
        return codeBlock;
    }
}
