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

public class FunctionCallExpression extends AbstractExpression {

    private final Expression memberExpr;
    private final List<Expression> argExprs;

    public FunctionCallExpression(Expression memberExpr, List<Expression> argExprs) {
        this.memberExpr = memberExpr;
        this.argExprs = argExprs;
    }
    
    public Position getPosition() {
        return this.memberExpr.getPosition();
    }
    
    public List<Expression> getArgumentExpressions() {
        return this.argExprs;
    }
    
    public Expression getMemberExpression() {
        return this.memberExpr;
    }
    
    public int getSizeMetric() {
        int size = this.memberExpr.getSizeMetric();
        
        for ( Expression each : argExprs ) {
            size += each.getSizeMetric();
        }
        
        return size + 5;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.memberExpr).append("(");
        boolean first = true;
        for (Expression each : this.argExprs) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(each.toString());
            first = false;

        }
        buf.append(")");
        return buf.toString();
    }
    
    public String dump(String indent) {
        StringBuilder buffer = new StringBuilder();
        buffer.append( super.dump( indent ) );
        buffer.append( this.memberExpr.dump( indent + "  " ) );
        for ( Expression arg : argExprs ) {
            buffer.append( arg.dump( indent + "  - " ) );
        }
        return buffer.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
}
