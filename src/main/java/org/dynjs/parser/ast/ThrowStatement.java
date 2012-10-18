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

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class ThrowStatement extends AbstractStatement {

    private final Expression expr;

    public ThrowStatement(final Tree tree, Expression expr) {
        super(tree);
        this.expr = expr;
    }
    
    public Expression getExpr()  {
        return this.expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append(expr.getCodeBlock());
                append(jsGetValue());
                // val
                newobj(p(ThrowException.class));
                // val ex
                dup_x1();
                // ex val ex
                swap();
                // ex ex val
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // ex ex val context
                swap();
                // ex ex context val
                invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class));
                // ex
                athrow();
            }
        };
    }

    public String toIndentedString(String indent) {
        return indent + "throw " + this.expr.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
