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
import org.dynjs.parser.ParserException;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class AssignmentOperationStatement extends BaseStatement implements Statement {

    private final Statement lhs;
    private final Statement rhs;

    public AssignmentOperationStatement(final Tree tree, final Statement lhs, final Statement rhs) {
        super(tree);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public CodeBlock getCodeBlock() {
        if (lhs instanceof ResolveByIndexStatement) {
            ResolveByIndexStatement statement = (ResolveByIndexStatement) lhs;
            final Statement targetObject = statement.getLhs();
            final Statement index = statement.getIndex();
            return new CodeBlock() {{
                append(targetObject.getCodeBlock());
                append(index.getCodeBlock());
                append(rhs.getCodeBlock());
                if (index instanceof StringLiteralStatement) {
                    invokedynamic("dyn:setProp", sig(Object.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
                } else {
                    invokedynamic("dyn:setElement", sig(void.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
                }
            }};
        } else if (lhs instanceof ResolveIdentifierStatement) {
            return new CodeBlock() {{
                append(lhs.getCodeBlock());
                append(rhs.getCodeBlock());
            }};
        }
        throw new ParserException("not implemented", getPosition());
    }
}
