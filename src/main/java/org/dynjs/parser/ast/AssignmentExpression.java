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

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class AssignmentExpression extends AbstractBinaryExpression {

    public AssignmentExpression(final Expression lhs, final Expression rhs) {
        super(lhs, rhs, "=");
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public String toString() {
        return getLhs() + " = " + getRhs();
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        if (getRhs() instanceof FunctionExpression) {
            decls.addAll(getLhs().getFunctionDeclarations());
            return decls;
        }
        return super.getFunctionDeclarations();
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object lhs = getLhs().interpret(context, debug);
        if (!(lhs instanceof Reference)) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError(lhs + " is not a reference"));
        }

        Reference lhsRef = (Reference) lhs;
        Object rhs = getValue(this.rhsGet, context, getRhs().interpret(context, debug));

        if (lhsRef.isUnresolvableReference() && context.isStrict()) {
            throw new ThrowException((ExecutionContext) context, ((ExecutionContext) context).createReferenceError(lhsRef.getReferencedName() + " is not defined"));
        }

        try {
            DynJSBootstrapper.getInvokeHandler().set(lhsRef, (ExecutionContext) context, lhsRef.getReferencedName(), rhs);
        } catch (ThrowException e) {
            throw e;
        } catch (Throwable e) {
            throw new ThrowException((ExecutionContext) context, e);
        }
        return(rhs);
    }
}
