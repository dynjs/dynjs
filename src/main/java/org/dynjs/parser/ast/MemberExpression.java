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
package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

/**
 * Access a property with dot notation
 * 
 * @see 11.2.1
 * 
 * @author Douglas Campos
 * @author Bob McWhirter
 */
public class MemberExpression extends AbstractBinaryExpression {

    public MemberExpression(final Tree tree, final Expression memberExpr, final Expression identifierExpr) {
        super(tree, memberExpr, identifierExpr, "." );
    }
    
    @Override
    public CodeBlock getCodeBlock() {
        // 11.2.1
        return new CodeBlock() {
            {
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // context
                append(getLhs().getCodeBlock());
                // context reference
                append(jsGetValue());
                // context object
                append(getRhs().getCodeBlock());
                // context object identifier-maybe-reference
                swap();
                // context identifier-maybe-reference obj
                append( jsCheckObjectCoercible() );
                // context identifier-maybe-reference obj
                swap();
                // context object identifier-maybe-reference
                append(jsGetValue());
                // context object identifier-obj
                append(jsToString());
                // context object identifier-str
                append(jsCreatePropertyReference());
                // reference
            }
        };
    }

    public String toString() {
        return getLhs() + "." + getRhs();
    }
    
    public String dump(String indent) {
        return super.dump(indent) + getLhs().dump( indent + "  " ) + getRhs().dump( indent + "  " );
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
