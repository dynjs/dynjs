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

public class StringLiteralExpression extends BaseExpression implements IllegalFunctionMemberExpression {

    private final String literal;
    
    private boolean continuedLine;
    private boolean escaped;

    public StringLiteralExpression(Position position, String literal) {
        super(position);
        this.literal = literal;
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    public void setContinuedLine(boolean continuedline) {
        this.continuedLine = continuedline;
    }
    
    public boolean isContinuedLine() {
        return this.continuedLine;
    }
    
    public void setEscaped(boolean escaped) {
        this.escaped = escaped;
    }
    
    public boolean isEscaped() {
        return this.escaped;
    }

    public String toString() {
        return this.literal;
    }
    
    public int getSizeMetric() {
        return 1;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        return this.literal;
    }

    public String dumpData() {
        return "\"" + this.literal + "\"";
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
}
