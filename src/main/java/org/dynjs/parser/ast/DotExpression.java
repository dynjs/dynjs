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

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

/**
 * Access a property with dot notation
 * 
 * see 11.2.1
 * 
 * @author Douglas Campos
 * @author Bob McWhirter
 */
public class DotExpression extends AbstractExpression {

    private Expression lhs;
    private String identifier;

    public DotExpression(Expression lhs, String identifier) {
        this.lhs = lhs;
        this.identifier = identifier;
    }
    
    public Position getPosition() {
        return this.lhs.getPosition();
    }
    
    public Expression getLhs() {
        return this.lhs;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public String toString() {
        return this.lhs + "." + this.identifier;
    }
    
    public int getSizeMetric() {
        return this.lhs.getSizeMetric() + 1;
    }
    
    public String dump(String indent) {
        return super.dump(indent) + getLhs().dump( indent + "  " ) + this.identifier;
    }

    @Override
    public void accept(Object context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
