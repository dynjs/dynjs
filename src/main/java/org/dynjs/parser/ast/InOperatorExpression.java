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
import org.dynjs.runtime.JSObject;
import org.objectweb.asm.tree.LabelNode;

public class InOperatorExpression extends AbstractBinaryExpression {

    public InOperatorExpression(final Tree tree, final Expression l, final Expression r) {
        super(tree, l, r, "in");
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode typeError = new LabelNode();
                LabelNode end = new LabelNode();
                
                append(getLhs().getCodeBlock());
                // obj(lhs)
                append(jsGetValue());
                // val(lhs)

                append(getRhs().getCodeBlock());
                // val(lhs) obj(rhs)
                append(jsGetValue());
                // val(lhs) val(rhs)

                dup();
                // val(lhs) val(rhs) val(rhs)
                instance_of(p(JSObject.class));
                // val(lhs) val(rhs) bool

                iffalse(typeError);
                // val(lhs) val(rhs)
                checkcast(p(JSObject.class));
                // val(lhs) obj(rhs)
                swap();
                // obj(rhs) val(lhs)
                append( jsToString() );
                // obj(rhs) str(lhs)
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj(rhs) str(lhs) context
                swap();
                // object(rhs) context str(lhs);
                invokeinterface(p(JSObject.class), "hasProperty", sig(boolean.class, ExecutionContext.class, String.class));
                // bool
                go_to(end);

                label(typeError);
                // val(lhs) val(rhs)
                pop();
                pop();
                iconst_0();
                i2b();
                //bool
                append(jsThrowTypeError("not an object"));

                label(end);
                invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                // Boolean
            }
        };
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
