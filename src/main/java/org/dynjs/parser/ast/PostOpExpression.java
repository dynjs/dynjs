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
                // Integer

                // ref number
                dup2();
                // ref Integer ref Integer
                invokevirtual(p(Number.class), "intValue", sig(int.class));
                // ref Integer ref int
                iconst_1();
                // ref Integer ref int 1
                if (getOp().equals("++")) {
                    iadd();
                } else {
                    isub();
                }
                // ref Integer(orig) ref int(new)
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // ref Integer(orig) ref int(new) context
                swap();
                // ref Integer(orig) ref context int(new)
                invokestatic(p(Integer.class), "valueOf", sig(Integer.class, int.class));
                // ref Integer(orig) ref context Integer(new)
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // ref Integer(orig)
                swap();
                // Integer(orig) ref
                pop();
                // Integer(orig)
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
}
