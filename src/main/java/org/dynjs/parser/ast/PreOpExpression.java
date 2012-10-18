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
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.objectweb.asm.tree.LabelNode;

public class PreOpExpression extends AbstractUnaryOperatorExpression {

    public PreOpExpression(final Tree tree, final Expression expression, String op) {
        super(tree, expression, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode storeNewValue = new LabelNode();
                LabelNode doubleNum = new LabelNode();
                LabelNode invalid = new LabelNode();
                LabelNode end = new LabelNode();

                append(getExpr().getCodeBlock());
                // obj
                dup();
                // obj obj
                instance_of(p(Reference.class));
                // ref bool
                iffalse(invalid);
                // ref
                dup();
                // ref ref
                invokevirtual(p(Reference.class), "isValidForPrePostIncrementDecrement", sig(boolean.class));
                // ref bool
                iffalse(invalid);
                // ref
                dup();
                // ref ref
                dup();
                // ref ref ref
                append(jsGetValue());
                // ref ref value
                append(jsToNumber());
                // ref ref number

                dup();
                // ref ref number number
                instance_of(p(Double.class));
                // ref ref number bool
                iftrue(doubleNum);
                // ref ref number

                // ----------------------------------------
                // Integral

                // ref ref number
                invokevirtual(p(Number.class), "longValue", sig(long.class));
                // ref ref long
                ldc( 1L );
                // ref ref long 1L
                if (getOp().equals("++")) {
                    ladd();
                } else {
                    lsub();
                }
                // ref ref long
                invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
                // ref ref Long
                go_to(storeNewValue);

                // ----------------------------------------
                // Double

                label(doubleNum);
                // ref ref number
                invokevirtual(p(Number.class), "doubleValue", sig(double.class));
                // ref ref double
                iconst_1();
                // ref ref double 1
                i2d();
                // ref ref double 1.0
                if (getOp().equals("++")) {
                    dadd();
                } else {
                    dsub();
                }
                // ref ref double
                invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
                // ref ref Double

                label(storeNewValue);
                // ref ref newval
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // ref ref newval context
                swap();
                // ref ref context newval
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // ref
                append(jsGetValue());
                // value
                go_to(end);

                label(invalid);
                // ref
                append(jsThrowSyntaxError());
                label(end);
                nop();

            }
        };
    }

    public String toString() {
        return getOp() + getExpr();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
