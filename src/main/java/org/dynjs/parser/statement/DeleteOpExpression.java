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
import org.dynjs.runtime.Reference;
import org.objectweb.asm.tree.LabelNode;

public class DeleteOpExpression extends AbstractExpression {

    private final Expression expr;

    public DeleteOpExpression(final Tree tree, final Expression expr) {
        super( tree );
        this.expr = expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode doNothing = new LabelNode();
                LabelNode testStrict = new LabelNode();
                append( expr.getCodeBlock() );
                // obj
                dup();
                // obj obj
                instance_of( p(Reference.class)  );
                // obj bool
                iffalse( doNothing );
                // ref
                dup();
                // ref ref
                invokevirtual( p(Reference.class), "isUnresolvableReference", sig( boolean.class ) );
                // ref bool
                iftrue( testStrict );
                // ref
                dup();
                // ref ref
                invokevirtual( p(Reference.class), "isPropertyReference", sig( boolean.class ) );
                
                

                // --------------------------------------
                // UNRESOLVABLE
                label( testStrict );
                // ref
                dup();
                // ref ref
                invokevirtual( p(Reference.class), "isStrictReference", sig( boolean.class ) );
                // ref bool
                iffalse( doNothing );
                // FIXME: throw Syntax
                
                label( doNothing );
                // X 
                pop();
                // <EMPTY>
                iconst_1();
                // true
            }
        };
    }

}
