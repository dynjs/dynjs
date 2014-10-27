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
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class ArrayLiteralExpression extends BaseExpression {

    private final List<Expression> exprs;
    private final List<CallSite> exprGets;

    public ArrayLiteralExpression(Position position, final List<Expression> exprs) {
        super(position);
        this.exprs = exprs;
        this.exprGets = new ArrayList<>();
        for (Expression each : exprs) {
            if (each != null) {
                this.exprGets.add(DynJSBootstrapper.factory().createGet(each.getPosition()));
            } else {
                this.exprGets.add( null );
            }
        }
        /*
        if (this.exprs.size() > 1 && (this.exprs.get(this.exprs.size() - 1) == null)) {
            this.exprs.remove(this.exprs.size() - 1);
        }
        */
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        for (Expression each : this.exprs) {
            if (each != null) {
                decls.addAll(each.getFunctionDeclarations());
            }
        }
        return decls;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        DynArray array = BuiltinArray.newArray(context);

        int numElements = this.exprs.size();
        int len = 0;
        for (int i = 0; i < numElements; ++i) {
            Expression each = this.exprs.get(i);
            Object value = null;
            if (each != null) {
                value = getValue(this.exprGets.get(i), context, each.interpret(context, debug));
                array.defineOwnProperty(context, "" + i, PropertyDescriptor.newPropertyDescriptorForObjectInitializer(value), false);
            }
            ++len;
        }
        array.put(context, "length", (long) len, true);

        return (array);
    }

    public List<Expression> getExprs() {
        return this.exprs;
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public int getSizeMetric() {
        int size = 0;

        for (Expression each : exprs) {
            if (each != null) {
                size += each.getSizeMetric();
            }
        }

        return size + 3;
    }

    public String dump(String indent) {
        StringBuilder buf = new StringBuilder(super.dump(indent));

        for (Expression expr : this.exprs) {
            buf.append(expr.dump(indent + "  "));
        }

        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        boolean first = true;
        for (Expression each : this.exprs) {
            if (!first) {
                buf.append(", ");
            }
            if (each != null) {
                buf.append(each.toString());
            }
        }
        buf.append("]");
        return buf.toString();
    }

}
