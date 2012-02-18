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

import java.util.ArrayList;
import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ArrayLiteralStatement implements Statement {
    private final List<Statement> exprs;

    public ArrayLiteralStatement(List<Statement> exprs) {
        this.exprs = exprs;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = newCodeBlock()
                .newobj(p(ArrayList.class))
                .dup()
                .pushInt(exprs.size())
                .invokespecial(p(ArrayList.class), "<init>", sig(void.class, int.class))
                .astore(4);

        for (Statement expr : exprs) {
            codeBlock = codeBlock
                    .aload(4)
                    .append(expr.getCodeBlock())
                    .invokevirtual(p(ArrayList.class), "add", sig(boolean.class, Object.class));

        }
        return codeBlock
                .aload(4);
    }
}
