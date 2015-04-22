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
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class ExpressionStatement extends AbstractStatement {

    private final Expression expr;
    private final CallSite get;

    public ExpressionStatement(final Expression expr) {
        this.expr = expr;
        this.get = DynJSBootstrapper.factory().createGet( expr.getPosition() );
    }

    public Position getPosition() {
        return this.expr.getPosition();
    }

    public Expression getExpr() {
        return this.expr;
    }

    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 1;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        Expression expr = getExpr();
        if (expr instanceof FunctionDeclaration) {
            return(Completion.createNormal());
        } else {
            return(Completion.createNormal(getValue(this.get, context, expr.interpret(context, debug))));
        }
    }

    public String dump(String indent) {
        return super.dump(indent) + this.expr.dump("  " + indent);
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.expr.getFunctionDeclarations();
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public String toIndentedString(String indent) {
        return indent + this.expr.toString();
    }
}
