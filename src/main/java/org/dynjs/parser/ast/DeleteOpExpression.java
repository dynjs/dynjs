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
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.objectweb.asm.tree.LabelNode;

public class DeleteOpExpression extends AbstractExpression {

    private final Expression expr;

    public DeleteOpExpression(final Tree tree, final Expression expr) {
        super(tree);
        this.expr = expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode checkAsProperty = new LabelNode();
                LabelNode handleEnvRec = new LabelNode();
                LabelNode returnTrue = new LabelNode();
                LabelNode end = new LabelNode();
                // ----------------------------------------

                append(expr.getCodeBlock());
                // ref
                dup();
                // ref ref
                instance_of(p(Reference.class));
                // ref bool
                iffalse(returnTrue);
                // ref
                dup();
                // ref ref
                invokevirtual(p(Reference.class), "isUnresolvableReference", sig(boolean.class));
                // ref bool
                iffalse( checkAsProperty );
                // ref
                dup();
                // ref ref
                invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
                // ref bool
                iffalse( returnTrue ); 
                // ref
                invokestatic(p(ExecutionContext.class), "throwSyntaxError", sig(void.class));
                // ref + throw
                go_to( returnTrue );

                // ----------------------------------------
                // Check as property
                
                label( checkAsProperty );
                ldc( "checking as property" ).aprintln().pop();
                // ref
                dup();
                // ref ref
                invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
                // ref bool
                iffalse( handleEnvRec );
                // ref 
                dup();
                // ref ref
                append( jsGetBase() );
                // ref base
                append( jsToObject() );
                // ref obj
                swap();
                // obj ref
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj ref context
                swap();
                // obj context ref
                dup();
                // obj context ref ref
                invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
                // obj context ref name
                swap();
                // obj context name ref
                invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
                // obj context name bool
                invokeinterface(p(JSObject.class), "delete", sig(boolean.class, ExecutionContext.class, String.class, boolean.class));
                // bool
                invokestatic( p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                // Boolean
                go_to( end );
                
                // ----------------------------------------
                // Environment record
                LabelNode throwSyntax = new LabelNode();
                
                label( handleEnvRec );
                // ref
                dup();
                // ref ref
                invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
                // ref bool
                iftrue( throwSyntax );
                // ref
                dup();
                // ref ref
                append( jsGetBase() );
                // ref base
                checkcast(p(EnvironmentRecord.class));
                // ref env-rec
                swap();
                // env-rec ref
                invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
                // env-rec name
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // env-rec name context
                swap();
                // env-rec context name
                invokeinterface( p(EnvironmentRecord.class), "deleteBinding", sig(boolean.class, ExecutionContext.class, String.class));
                // bool
                invokestatic( p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
                // Boolean
                go_to(end);
                
                
                
                label( throwSyntax );
                // ref
                invokestatic(p(ExecutionContext.class), "throwSyntaxError", sig(void.class));  
                // ref
                go_to( end );

                // ----------------------------------------
                // Simple true (with pop)
                label(returnTrue);
                // ref
                pop();
                // <EMPTY>
                getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
                // Boolean

                // ----------------------------------------
                label(end);
                nop();
            }
        };
    }

}
