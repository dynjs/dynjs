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

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class WhileStatement extends AbstractIteratingStatement {

    private final Expression vbool;
    private final Statement vloop;

    public WhileStatement(Position position, Expression vbool, Statement vloop) {
        super(position);
        this.vbool = vbool;
        this.vloop = vloop;
    }

    public Expression getTest() {
        return this.vbool;
    }

    public Statement getBlock() {
        return this.vloop;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.vloop.getVariableDeclarations();
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append(indent).append("while (").append(this.vbool.toString()).append(") {\n");
        buf.append(this.vloop.toIndentedString(indent + "  "));
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
}
