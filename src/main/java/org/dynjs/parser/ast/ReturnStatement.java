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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class ReturnStatement extends BaseStatement {

    private final Expression expr;
    private final CallSite get;

    public ReturnStatement(final Position position, final Expression expr) {
        super(position);
        this.expr = expr;
        if (this.expr != null) {
            this.get = DynJSBootstrapper.factory().createGet(expr.getPosition());
        } else {
            this.get = null;
        }
    }

    public Expression getExpr() {
        return this.expr;
    }

    public int getSizeMetric() {
        if (this.expr == null) {
            return 1;
        }

        return this.expr.getSizeMetric() + 1;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        if (this.expr != null) {
            Object value = this.expr.interpret(context, debug);
            return (Completion.createReturn(getValue(this.get, context, value)));
        } else {
            return (Completion.createReturn(Types.UNDEFINED));
        }
    }

    public String toIndentedString(String indent) {
        return indent + "return" + (this.expr == null ? "" : " " + this.expr.toString());
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    @Override
    public String dump(String indent) {
        return super.dump(indent) + (this.expr == null ? "" : this.expr.dump(indent + "  "));
    }
}
