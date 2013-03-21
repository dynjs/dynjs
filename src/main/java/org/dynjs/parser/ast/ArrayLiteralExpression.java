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

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ArrayLiteralExpression extends BaseExpression {

    private final List<Expression> exprs;

    public ArrayLiteralExpression(Position position, final List<Expression> exprs) {
        super(position);
        this.exprs = exprs;
        /*
        if (this.exprs.size() > 1 && (this.exprs.get(this.exprs.size() - 1) == null)) {
            this.exprs.remove(this.exprs.size() - 1);
        }
        */
    }
    
    public List<FunctionDeclaration> getFunctionDeclarations() {
        List<FunctionDeclaration> decls = new ArrayList<>();
        for ( Expression each : this.exprs ) {
            if ( each != null ) {
                decls.addAll( each.getFunctionDeclarations() );
            }
        }
        return decls;
    }

    public List<Expression> getExprs() {
        return this.exprs;
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
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

    public String toString() {
        StringBuffer buf = new StringBuffer();
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
