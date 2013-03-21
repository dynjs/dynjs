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

public class DoWhileStatement extends AbstractIteratingStatement {

    private final Expression test;
    private final Statement block;

    public DoWhileStatement(Position position, final Statement block, final Expression test) {
        super(position);
        this.block = block;
        this.test = test;
    }

    public Expression getTest() {
        return this.test;
    }

    public Statement getBlock() {
        return this.block;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
    
    public int getSizeMetric() {
        return test.getSizeMetric() + 5;
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("do {\n");
        buf.append(indent).append("} while (").append(test.toString()).append(")");
        return buf.toString();
    }
}
