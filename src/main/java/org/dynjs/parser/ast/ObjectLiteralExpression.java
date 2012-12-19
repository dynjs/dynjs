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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class ObjectLiteralExpression extends BaseExpression {

    private final List<PropertyAssignment> propertyAssignments;

    public ObjectLiteralExpression(Position position, final List<PropertyAssignment> propertyAssignments) {
        super(position);
        this.propertyAssignments = propertyAssignments;
    }
    
    public List<PropertyAssignment> getPropertyAssignments() {
        return this.propertyAssignments;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{ ");
        boolean first = true;

        for (PropertyAssignment each : this.propertyAssignments) {
            if (!first) {
                buf.append(", ");
            }
            first = false;
            buf.append(each.toString());

        }
        buf.append(" }");

        return buf.toString();
    }
    
    public int getSizeMetric() {
        int size = 0;
        
        for ( PropertyAssignment each : this.propertyAssignments ) {
            size += each.getSizeMetric();
        }
        
        return size + 5;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
