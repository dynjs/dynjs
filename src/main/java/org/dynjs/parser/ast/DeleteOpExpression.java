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
import org.dynjs.runtime.*;

import java.util.Map;

public class DeleteOpExpression extends AbstractUnaryOperatorExpression {

    public DeleteOpExpression(final Expression expr) {
        super(expr, "delete");
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context) {

        Object result = getExpr().interpret(context);
        if (!(result instanceof Reference)) {
            return (true);

        }

        Reference ref = (Reference) result;
        if (ref.isUnresolvableReference()) {
            if (context.isStrict()) {
                throw new ThrowException(context, context.createSyntaxError("cannot delete unresolvable reference"));
            } else {
                return (true);

            }
        }

        if (ref.isPropertyReference()) {
            Object base = ref.getBase();
            if ( ( ! ( base instanceof JSObject) ) && base instanceof Map) {
                return  ( ((Map) base).remove( ref.getReferencedName() ) != null );
            }
            return (Types.toObject(context, ref.getBase()).delete(context, ref.getReferencedName(), ref.isStrictReference()));

        }

        if (ref.isStrictReference()) {
            throw new ThrowException(context, context.createSyntaxError("cannot delete from environment record binding"));
        }

        EnvironmentRecord bindings = (EnvironmentRecord) ref.getBase();

        return (bindings.deleteBinding(context, ref.getReferencedName()));
    }

    public String toString() {
        return "delete " + getExpr();
    }

}
