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
 * An Identifier is evaluated by performing Identifier Resolution as specified
 * in 10.3.1.
 * The result of evaluating an Identifier is always a value of type Reference.
 * 
 * see 11.1.2
 * see 10.3.1
 * 
 * @author Douglas Campos
 * @author Bob McWhirter
 */
public class IdentifierReferenceExpression extends BaseExpression {

    private final String identifier;

    public IdentifierReferenceExpression(Position position, String identifier) {
        super(position);
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return this.identifier;
    }

    public String dumpData() {
        return identifier;
    }
    
    public int getSizeMetric() {
        return 2;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    public Object interpret(ExecutionContext context, boolean debug) {
        return context.resolve(getIdentifier());
    }

}
