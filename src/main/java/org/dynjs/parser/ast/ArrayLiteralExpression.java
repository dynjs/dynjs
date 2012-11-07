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
package org.dynjs.parser.ast;

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class ArrayLiteralExpression extends AbstractExpression {

    private final List<Expression> exprs;

    public ArrayLiteralExpression(final Tree tree, final List<Expression> exprs) {
        super(tree);
        this.exprs = exprs;
        if (this.exprs.get(this.exprs.size() - 1) == null) {
            this.exprs.remove(this.exprs.size() - 1);
        }
    }

    public List<Expression> getExprs() {
        return this.exprs;
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        boolean first = true;
        for (Expression each : this.exprs) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(each.toString());
        }
        buf.append("]");
        return buf.toString();
    }

}
