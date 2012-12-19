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
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ForExprStatement extends AbstractForStatement {

    private final Expression initialize;

    public ForExprStatement(Position position, Expression initialize, Expression test, Expression increment, Statement block) {
        super(position, test, increment, block);
        this.initialize = initialize;
    }
    
    public Expression getExpr() {
        return this.initialize;
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("for (").append(this.initialize == null ? "" : this.initialize.toString()).append(" ; ");
        buf.append((getTest() == null ? "" : getTest().toString())).append(" ; ");
        buf.append((getIncrement() == null ? "" : getIncrement().toString())).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
    
    public int getSizeMetric() {
        if ( this.initialize != null ) {
            return this.initialize.getSizeMetric() + super.getSizeMetric();
        }
        return super.getSizeMetric();
    }
}
