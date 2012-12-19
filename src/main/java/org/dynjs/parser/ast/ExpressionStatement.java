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

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ExpressionStatement extends AbstractStatement {

    private final Expression expr;

    public ExpressionStatement(final Expression expr) {
        this.expr = expr;
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
    
    public String dump(String indent) {
        return super.dump(indent) + this.expr.dump("  " + indent);
    }
    
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict);
    }

    public String toIndentedString(String indent) {
        return indent + this.expr.toString();
    }
}
