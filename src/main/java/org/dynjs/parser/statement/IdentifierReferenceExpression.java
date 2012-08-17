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
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;

/**
 * An Identifier is evaluated by performing Identifier Resolution as specified
 * in 10.3.1.
 * The result of evaluating an Identifier is always a value of type Reference.
 * 
 * @see 11.1.2
 * @see 10.3.1
 * 
 * @author Douglas Campos
 * @author Bob McWhirter
 */
public class IdentifierReferenceExpression extends AbstractExpression {

    private final String identifier;

    public IdentifierReferenceExpression(final Tree tree, final String identifier) {
        super(tree);
        this.identifier = identifier;
    }

    @Override
    public CodeBlock getCodeBlock() {
        // 10.3.1
        return new CodeBlock() {
            {
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // context
                ldc(identifier);
                // context identifier
                invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
                // reference
            }
        };
    }
}
