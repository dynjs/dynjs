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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class VariableStatement extends BaseStatement {

    private List<VariableDeclaration> decls;

    public VariableStatement(Position position, final List<VariableDeclaration> decls) {
        super( position );
        this.decls = decls;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.decls;
    }
    
    public String dump(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(super.dump(indent));
        for (VariableDeclaration decl : this.decls) {
            buf.append(decl.dump(indent + "  "));
        }
        return buf.toString();
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("var ");
        boolean first = true;
        for (VariableDeclaration each : decls) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(each.toString());
            first = false;
        }
        return buf.toString();
    }
    
    public int getSizeMetric() {
        int size = 0;
        for ( VariableDeclaration each : this.decls ) {
            size += each.getSizeMetric();
        }
        
        return size + 2;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
