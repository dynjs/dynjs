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
import org.dynjs.compiler.JSCompiler;

/**
 * Access a property with dot notation
 * 
 * @see 11.2.1
 * 
 * @author Douglas Campos
 * @author Bob McWhirter
 */
public class PropertyAccessorExpression extends AbstractExpression {

    private Expression memberExpr;
    private Expression identifierExpr;

    public PropertyAccessorExpression(final Tree tree, final Expression memberExpr, final Expression identifierExpr) {
        super( tree );
        this.memberExpr = memberExpr;
        this.identifierExpr = identifierExpr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        // 11.2.1
        return new CodeBlock() {
            {
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // context
                append( memberExpr.getCodeBlock() );
                // context reference
                append( jsGetValue() );
                // context object
                append( identifierExpr.getCodeBlock() );
                // context object identifier-maybe-reference
                append( jsGetValue() );
                // context object identifier-obj
                append( jsToString() );
                // context object identifier-str
                append( jsCreatePropertyReference() );
                // reference
            }
        };
    }
}
