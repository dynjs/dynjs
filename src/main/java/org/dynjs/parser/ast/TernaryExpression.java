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

public class TernaryExpression extends AbstractExpression {

    private final Expression vbool;
    private final Expression vthen;
    private final Expression velse;

    public TernaryExpression(final Expression vbool, final Expression vthen, final Expression velse) {
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }
    
    public Position getPosition() {
        return this.vbool.getPosition();
    }
    
    public Expression getTest() {
        return this.vbool;
    }
    
    public Expression getThenExpr() {
        return this.vthen;
    }
    
    public Expression getElseExpr() {
        return this.velse;
    }
    
    public int getSizeMetric() {
        return this.vbool.getSizeMetric() + this.vthen.getSizeMetric() + this.velse.getSizeMetric() + 5;
    }

    public String toString() {
        return this.vbool + " ? " + this.vthen + " : " + this.velse;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
