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

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ForStepVarStatement implements Statement {

    private final Statement varDef;
    private final Statement expr1;
    private final Statement expr2;
    private final BlockStatement statement;

    public ForStepVarStatement(Statement varDef, Statement expr1, Statement expr2, Statement statement) {
        this.varDef = varDef;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.statement = (BlockStatement) statement;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .append(varDef.getCodeBlock())
                .label(statement.getBeginLabel())
                .append(expr1.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(statement.getEndLabel())
                .append(statement.getCodeBlock())
                .append(expr2.getCodeBlock())
                .go_to(statement.getBeginLabel())
                .label(statement.getEndLabel());
    }
}
