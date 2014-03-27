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
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ForVarDeclOfStatement extends AbstractForInStatement {

    private final VariableDeclaration decl;

    public ForVarDeclOfStatement(Position position, VariableDeclaration decl, Expression rhs, Statement block) {
        super(position, rhs, block);
        this.decl = decl;
    }

    public VariableDeclaration getDeclaration() {
        return this.decl;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<VariableDeclaration>(super.getVariableDeclarations());
        decls.add(decl);
        return decls;
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("for (").append(decl).append(" of ").append(getRhs().toString()).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }
    
    public int getSizeMetric() {
        return super.getSizeMetric() + decl.getSizeMetric();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

}
