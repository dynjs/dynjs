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

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class BitwiseInversionOperatorExpression extends AbstractUnaryOperatorExpression {

    public BitwiseInversionOperatorExpression(final Tree tree, final Expression expr) {
        super(tree, expr, "~");
    }
    
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append(getExpr().getCodeBlock());
                // obj
                append(jsGetValue());
                // val
                append(jsToInt32());
                // Long
                invokevirtual(p(Long.class), "longValue", sig(long.class));
                // long
                ldc( -1L );
                // long -1(long)
                lxor();
                // long
                invokestatic( p(Long.class), "valueOf", sig(Long.class, long.class));
                // Long
            }
        };
    }

    public String toString() {
        return "~" + getExpr();
    }

}