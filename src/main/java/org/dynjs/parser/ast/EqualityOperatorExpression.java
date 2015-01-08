/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

public class EqualityOperatorExpression extends AbstractBinaryExpression {

    public EqualityOperatorExpression(final Expression lhs, final Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object lhs = getValue(this.lhsGet, context, getLhs().interpret( context, debug) );
        Object rhs = getValue(this.rhsGet, context, getRhs().interpret(context, debug));

        if (getOp().equals("==")) {
            return(Types.compareEquality(context, lhs, rhs));
        } else {
            return(!Types.compareEquality(context, lhs, rhs));
        }
    }

}
