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
import org.objectweb.asm.tree.LabelNode;

public class PreOpExpression extends AbstractUnaryOperatorExpression {

    public PreOpExpression(final Tree tree, final Expression expression, String op) {
        super( tree, expression, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode invalid = new LabelNode();
                LabelNode end = new LabelNode();

                append( getExpr().getCodeBlock() );
                // obj
                dup();
                // obj obj
                instance_of( p( Reference.class ) );
                // ref bool
                iffalse( invalid );
                invokevirtual( p( Reference.class ), "isValidForPrePostIncrementDecrement", sig( boolean.class ) );
                // ref bool
                iffalse( invalid );
                // ref
                dup();
                // ref ref
                dup();
                // ref ref ref
                append( jsGetValue() );
                // ref ref value
                append( jsToNumber() );
                // ref ref number
                iconst_1();
                // ref ref number 1
                if (getOp().equals( "++" )) {
                    iadd();
                } else {
                    isub();
                }
                // ref ref newval
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // ref ref newval context
                swap();
                // ref ref context newval
                invokevirtual( p( Reference.class ), "putValue", sig( void.class, ExecutionContext.class, Object.class ) );
                // ref
                append( jsGetValue() );
                // value
                go_to( end );

                label( invalid );
                // ref
                pop();
                // empty
                // TODO: throw?
                label( end );

            }
        };
    }

}
