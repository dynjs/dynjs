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
import org.dynjs.runtime.DynObject;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ObjectLiteralStatement extends BaseStatement implements Statement {

    private final List<Statement> namedValues;

    public ObjectLiteralStatement(final Tree tree, final List<Statement> namedValues) {
        super(tree);
        this.namedValues = namedValues;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            newobj(p(DynObject.class));
            dup();
            invokespecial(p(DynObject.class), "<init>", sig(void.class));
            dup();
            astore(7);
            for (Statement namedValue : namedValues) {
                aload(7);
                append(namedValue.getCodeBlock());
            }
            aload(7);
        }};
    }
}
