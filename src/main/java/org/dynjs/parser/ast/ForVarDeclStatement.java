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

import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ForVarDeclStatement extends AbstractForStatement {

    private final List<VariableDeclaration> declList;

    public ForVarDeclStatement(Position position, List<VariableDeclaration> declList, Expression test, Expression increment, Statement block) {
        super(position, test, increment, block);
        this.declList = declList;
    }

    public List<VariableDeclaration> getDeclarationList() {
        return this.declList;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        decls.addAll(declList);
        decls.addAll(super.getVariableDeclarations() );
        return decls;
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("for (").append(this.declList == null ? "" : this.declList).append(" ; ");
        buf.append((getTest() == null ? "" : getTest().toString())).append(" ; ");
        buf.append((getIncrement() == null ? "" : getIncrement().toString())).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }
    
    public int getSizeMetric() {
        int size = super.getSizeMetric();
        
        for ( VariableDeclaration each : this.declList ) {
            size += each.getSizeMetric();
        }
        
        return size;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }

}
