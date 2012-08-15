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
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class StrictEqualityOperatorExpression extends AbstractBinaryExpression {

    public StrictEqualityOperatorExpression(final Tree tree, final Expression l, final Expression r, String op) {
        super( tree, l, r, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        // 11.9
        return new CodeBlock() {
            {
                LabelNode returnTrue = new LabelNode();
                LabelNode returnFalse = new LabelNode();
                LabelNode end = new LabelNode();
                
                append( getLhs().getCodeBlock() );
                // obj(lhs)
                append( jsGetValue() );
                // val(lhs)
                append( getRhs().getCodeBlock() );
                // val(lhs) obj(rhs)
                append( jsGetValue() );
                // val(lhs) val(rhs)
                invokestatic( p(Types.class), "compareStrictEquality", sig( boolean.class, Object.class, Object.class ) );
                // bool
                
                if ( getOp().equals(  "==="  ) ) {
                    iftrue( returnTrue );
                    go_to( returnFalse );
                } else {
                    iffalse( returnTrue );
                    go_to( returnFalse );
                }
                
                label(returnTrue);
                getstatic( p(Boolean.class), "TRUE", ci(Boolean.class) );
                go_to(end);
                
                label(returnFalse);
                getstatic( p(Boolean.class), "FALSE", ci(Boolean.class) );
                
                label(end);
                nop();
                
            }
        };
    }
    
}
