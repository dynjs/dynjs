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
import org.dynjs.runtime.ExecutionContext;

public class NewOperatorExpression extends AbstractUnaryOperatorExpression {

    private List<Expression> argExprs;

    public NewOperatorExpression(final Expression expr) {
        this( expr, new ArrayList<Expression>() );
    }
    
    public NewOperatorExpression(final Expression expr, final List<Expression> argExprs) {
        super(expr, "new" );
        this.argExprs = argExprs;
    }
    
    public List<Expression> getArgumentExpressions() {
        return this.argExprs;
    }
    
    public String toString() {
        return "new " + getExpr();
    }
    
    public String dump(String indent) {
        return super.dump(indent) + "new " + getExpr().dump( indent + "  " );
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict);
    }
}
