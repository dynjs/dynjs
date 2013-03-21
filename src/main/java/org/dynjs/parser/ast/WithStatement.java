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

import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class WithStatement extends BaseStatement {

    private final Expression expr;
    private final Statement block;

    public WithStatement(Position position, Expression expr, Statement block) {
        super( position );
        this.expr = expr;
        this.block = block;
    }
    
    public Expression getExpr() {
        return this.expr;
    }
    
    public Statement getBlock() {
        return this.block;
    }
    
    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 3;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }
    
    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("with (").append(this.expr.toString()).append(") {\n");
        buf.append(this.block.toIndentedString(indent + "  "));
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict);
    }
}
