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

public class PostOpExpression extends AbstractUnaryOperatorExpression {

    public PostOpExpression(final Tree tree, final Expression expression, String op) {
        super(tree, expression, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
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
                append(jsGetValue());
                // ref value
                append(jsToNumber());
                // ref number
                dup();
                // ref number number
                instance_of(p(Double.class));
                // ref number bool
                iftrue(doubleNum);

                // ----------------------------------------
                // Long

                // ref number
                dup2();
                // ref Long ref Long
                invokevirtual(p(Number.class), "longValue", sig(long.class));
                // ref Long ref long
                ldc( 1L );
                // ref Long ref long 1
                if (getOp().equals("++")) {
                    ladd();
                } else {
                    lsub();
                }
                // ref Long(orig) ref long(new)
                invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
                // ref Long(orig) ref Long(new)
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // ref Long(orig) ref Long(new) context
                swap();
                // ref Long(orig) ref context Long(new)
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // ref Long(orig)
                swap();
                // Long(orig) ref
                pop();
                // Long(orig)
                go_to(end);

                // ----------------------------------------
                // Double

                label(doubleNum);

                // ref number
                dup2();
                // ref Double ref Double
                invokevirtual(p(Number.class), "doubleValue", sig(double.class));
                // ref Double ref double
                iconst_1();
                // ref Double ref double 1
                i2d();
                // ref Double ref double 1.0
                if (getOp().equals("++")) {
                    dadd();
                } else {
                    dsub();
                }
                // ref Double(orig) ref double(new)
                invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
                // ref Double(orig) ref Double(new)
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // ref Double(orig) ref Double(new) context
                swap();
                // ref Double(orig) ref context Double(new)
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // ref Double(orig)
                swap();
                // Double(orig) ref
                pop();
                // Double(orig)
                go_to(end);

                // ----------------------------------------
                // Invalid
                label(invalid);
                // ref
                append(jsThrowSyntaxError());

                label(end);
                nop();

            }
        };
    }

    public String toString() {
        return getExpr() + getOp();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
