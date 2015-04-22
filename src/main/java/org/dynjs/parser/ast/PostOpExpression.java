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

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class PostOpExpression extends AbstractUnaryOperatorExpression {

    private final CallSite get;

    public PostOpExpression(final Expression expr, String op) {
        super(expr, op);
        this.get = DynJSBootstrapper.factory().createGet( expr.getPosition() );
    }

    public String toString() {
        return getExpr() + getOp();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object lhs = getExpr().interpret(context, debug);

        if (lhs instanceof Reference) {
            if (((Reference) lhs).isStrictReference()) {
                if (((Reference) lhs).getBase() instanceof EnvironmentRecord) {
                    if (((Reference) lhs).getReferencedName().equals("arguments") || ((Reference) lhs).getReferencedName().equals("eval")) {
                        throw new ThrowException(context, context.createSyntaxError("invalid assignment: " + ((Reference) lhs).getReferencedName()));
                    }
                }
            }

            Number newValue = null;
            Number oldValue = Types.toNumber(context, getValue(this.get, context, lhs));

            if (oldValue instanceof Double) {
                switch (getOp()) {
                    case "++":
                        newValue = oldValue.doubleValue() + 1;
                        break;
                    case "--":
                        newValue = oldValue.doubleValue() - 1;
                        break;
                }
            } else {
                switch (getOp()) {
                    case "++":
                        newValue = oldValue.longValue() + 1;
                        break;
                    case "--":
                        newValue = oldValue.longValue() - 1;
                        break;
                }
            }

            ((Reference) lhs).putValue(context, newValue);
            return (oldValue);
        }

        return null;
    }
}
