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
import org.dynjs.api.Function;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

public class IfStatement extends BaseStatement implements Statement {

    private final DynThreadContext context;
    private final Statement vbool;
    private final Statement vthen;
    private final Statement velse;
    private boolean asExpression;

    public IfStatement(final Tree tree, final DynThreadContext context, final Statement vbool, final Statement vthen, final Statement velse, boolean asExpression) {
        super( tree );
        this.context = context;
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
        this.asExpression = asExpression;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = new CodeBlock() {
            {

                LabelNode hoistReturn = new LabelNode();
                LabelNode skipReturn = new LabelNode();

                aload( DynJSCompiler.Arities.SELF );
                aload( DynJSCompiler.Arities.CONTEXT );

                append( CodeBlockUtils.compileBasicBlockWithReturn( context, "If", vbool ) );
                // self context function(if)

                if (asExpression) {
                    append( CodeBlockUtils.compileBasicBlockWithReturn( context, "Then", vthen ) );
                    // self context function(if) function(then)
                    append( CodeBlockUtils.compileBasicBlockWithReturn( context, "Else", velse ) );
                    // self context function(if) function(then) function(else)
                } else {
                    append( CodeBlockUtils.compileBasicBlock( context, "Then", vthen ) );
                    // self context function(if) function(then)
                    append( CodeBlockUtils.compileBasicBlock( context, "Else", velse ) );
                    // self context function(if) function(then) function(else)
                }

                invokestatic( p( RT.class ), "ifThenElse", sig( Object.class, Object.class, DynThreadContext.class, Function.class, Function.class, Function.class ) );
                if (asExpression) {
                    // hey, nothing, leave it on the stack.
                } else {
                    dup();
                    ifnonnull( hoistReturn );
                    pop(); // remove the null
                    go_to( skipReturn );
                    label( hoistReturn );
                    areturn();
                    label( skipReturn );
                    nop();
                }
            }
        };
        return codeBlock;
    }
}
