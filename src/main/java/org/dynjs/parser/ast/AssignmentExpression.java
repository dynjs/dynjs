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
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.objectweb.asm.tree.LabelNode;

public class AssignmentExpression extends AbstractExpression {

    private final Expression lhs;
    private final Expression rhs;

    public AssignmentExpression(final Tree tree, final Expression lhs, final Expression rhs) {
        super(tree);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode throwRefError = new LabelNode();
                LabelNode end = new LabelNode();
                
                append(lhs.getCodeBlock());
                // reference
                dup();
                // reference reference
                instance_of(p(Reference.class) );
                // reference bool
                iffalse(throwRefError);
                // reference
                checkcast( p(Reference.class));
                append(rhs.getCodeBlock());
                // reference expr
                append(jsGetValue());
                // reference value
                dup_x1();
                // value reference value
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // value reference value context
                swap();
                // value reference context value
                invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                // value
                go_to( end );
                
                label( throwRefError );
                // reference
                pop();
                
                newobj(p(ThrowException.class));
                // ex
                dup();
                // ex ex
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // ex ex context
                ldc( lhs.toString() + " is not a reference" );
                // ex ex context str
                invokevirtual(p(ExecutionContext.class), "createReferenceError", sig(JSObject.class, String.class));
                // ex ex error
                invokespecial(p(ThrowException.class), "<init>", sig(void.class, Object.class));
                // ex ex
                athrow();
                
                
                label(end );
                nop();
            }
        };
    }

    public String toString() {
        return lhs + " = " + rhs;
    }
}
