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

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;

public class NewOperatorExpression extends AbstractExpression {

    private final Expression newExpr;
    private final List<Expression> argExprs;

    public NewOperatorExpression(final Tree tree, final Expression newExpr, final List<Expression> argExprs) {
        super(tree);
        this.newExpr = newExpr;
        this.argExprs = argExprs;
    }
    
    @Override
    public void verify(ExecutionContext context, boolean strict) {
        this.newExpr.verify(context, strict);
        for ( Expression each : argExprs ) {
            each.verify(context, strict);
        }
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 11.2.2
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // context
                append(newExpr.getCodeBlock());
                // context reference
                append(jsGetValue(JSFunction.class));
                // context ctor-fn

                int numArgs = argExprs.size();
                bipush(numArgs);
                anewarray(p(Object.class));
                // context function array
                for (int i = 0; i < numArgs; ++i) {
                    dup();
                    bipush(i);
                    append(argExprs.get(i).getCodeBlock());
                    append(jsGetValue());
                    aastore();
                }
                // context function array
                invokevirtual(p(ExecutionContext.class), "construct", sig(JSObject.class, JSFunction.class, Object[].class));
                // obj
            }
        };
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.newExpr).append("(");
        boolean first = true;
        for (Expression each : this.argExprs) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(each.toString());
            first = false;

        }
        buf.append(")");
        return buf.toString();
    }
}
