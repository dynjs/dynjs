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

import me.qmx.jitescript.CodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.*;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class TernaryExpression extends AbstractExpression {

    private final Expression vbool;
    private final Expression vthen;
    private final Expression velse;

    public TernaryExpression(final Tree tree, final Expression vbool, final Expression vthen, final Expression velse) {
        super( tree );
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode elseBranch = new LabelNode();
                LabelNode end = new LabelNode();

                append( vbool.getCodeBlock() );
                // val
                append( jsToBoolean() );
                // Boolean
                invokevirtual( p(Boolean.class), "booleanValue", sig(boolean.class) );
                // bool
                iffalse( elseBranch );
                // <empty>
                append( vthen.getCodeBlock() );
                // thenval
                go_to(end);
                
                label( elseBranch );
                append( velse.getCodeBlock() );
                // elseval
                label( end );
                nop();
            }
        };
    }
}
