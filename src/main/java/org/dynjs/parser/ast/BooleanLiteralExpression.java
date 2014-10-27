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

public class BooleanLiteralExpression extends BaseExpression implements IllegalFunctionMemberExpression {

    private final boolean value;

    public BooleanLiteralExpression(final Position position, final boolean value) {
        super(position);
        this.value = value;
    }
    
    public boolean getValue() {
        return this.value;
    }
    
    public String toString() {
        return "" + this.value;
    }
    
    public int getSizeMetric() {
        return 1;
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    @Override
    public String dumpData() {
        return "" + value;
    }

    public Object interpret(ExecutionContext context, boolean debug) {
        return(getValue());
    }
}
