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

public abstract class NumberLiteralExpression extends BaseExpression {

    private final String text;
    // FIXME: Radix is unneeded since concrete types already know their value.
    // This is only left in for current JIT to continue working and should be
    // removed once the new IR is working.
    private final int radix;

    public NumberLiteralExpression(Position position, String text, int radix) {
        super(position);

        this.text = text;
        this.radix = radix;
    }

    public String getText() {
        return this.text;
    }

    public int getRadix() {
        return this.radix;
    }

    public String toString() {
        return this.text;
    }
    
    public int getSizeMetric() {
        return 3;
    }
}
