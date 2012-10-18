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
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class FunctionCallExpression extends AbstractExpression {

    private final Expression memberExpr;
    private final List<Expression> argExprs;

    public FunctionCallExpression(final Tree tree, final Expression memberExpr, final List<Expression> argExprs) {
        super(tree);
        this.memberExpr = memberExpr;
        this.argExprs = argExprs;
    }
    
    public List<Expression> getArgumentExpressions() {
        return this.argExprs;
    }
    
    public Expression getMemberExpression() {
        return this.memberExpr;
    }
    
    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode propertyRef = new LabelNode();
                LabelNode noSelf = new LabelNode();
                LabelNode doCall = new LabelNode();
                LabelNode isCallable = new LabelNode();
                // 11.2.3
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // context
                append(memberExpr.getCodeBlock());
                // context ref
                dup();
                // context ref ref
                append(jsGetValue());
                // context ref function
                dup();
                // context ref function function
                invokestatic(p(Types.class), "isCallable", sig(boolean.class, Object.class));
                // context ref function bool
                iftrue(isCallable);
                // context ref function 
                append(jsThrowTypeError(memberExpr + " is not a function"));
                // THROWN!

                // ----------------------------------------
                // Is Callable

                label(isCallable);
                // context ref function
                swap();
                // context function ref
                dup();
                // context function ref ref
                instance_of(p(Reference.class));
                // context function ref isref?
                iffalse(noSelf);

                // ----------------------------------------
                // Reference

                // context function ref
                checkcast(p(Reference.class));

                dup();
                // context function ref ref
                invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
                // context function ref bool(is-prop)

                iftrue(propertyRef);

                // ----------------------------------------
                // Environment Record

                // context function ref
                append(jsGetBase());
                // context function base
                checkcast(p(EnvironmentRecord.class));
                // context function env-rec
                invokeinterface(p(EnvironmentRecord.class), "implicitThisValue", sig(Object.class));
                // context function self
                go_to(doCall);

                // ----------------------------------------
                // Property Reference
                label(propertyRef);
                // context function ref
                append(jsGetBase());
                // context function self
                go_to(doCall);

                // ------------------------------------------
                // No self
                label(noSelf);
                // context function ref
                pop();
                // context function
                append(jsPushUndefined());
                // context function UNDEFINED

                // ------------------------------------------
                // call()

                label(doCall);
                // context function self
                
                aload(JSCompiler.Arities.EXECUTION_CONTEXT );
                // context function self context
                invokevirtual(p(ExecutionContext.class), "pushCallContext", sig(void.class));
                // context function self 

                int numArgs = argExprs.size();
                bipush(numArgs);
                anewarray(p(Object.class));
                // context function self array
                for (int i = 0; i < numArgs; ++i) {
                    dup();
                    bipush(i);
                    append(argExprs.get(i).getCodeBlock());
                    append(jsGetValue());
                    aastore();
                }
                // context function self array
                
                aload(JSCompiler.Arities.EXECUTION_CONTEXT );
                // context function self array context
                invokevirtual(p(ExecutionContext.class), "popCallContext", sig(void.class));
                // context function self array
                
                // call ExecutionContext#call(fn, self, args) -> Object
                invokevirtual(p(ExecutionContext.class), "call", sig(Object.class, JSFunction.class, Object.class, Object[].class));
                // obj
            }
        };
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.memberExpr).append("(");
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
    
    public String dump(String indent) {
        return super.dump(indent) + this.memberExpr.dump( indent + "  " );
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
        
    }
}
