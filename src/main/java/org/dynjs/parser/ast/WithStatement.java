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

import java.lang.invoke.CallSite;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class WithStatement extends BaseStatement {

    private final CallSite get;

    private final Expression expr;
    private final Statement block;

    public WithStatement(Position position, Expression expr, Statement block) {
        super( position );
        this.expr = expr;
        this.block = block;
        this.get = DynJSBootstrapper.factory().createGet( this.expr.getPosition() );
    }
    
    public Expression getExpr() {
        return this.expr;
    }
    
    public Statement getBlock() {
        return this.block;
    }
    
    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 3;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        JSObject obj = Types.toObject(context, getValue(this.get, context, getExpr().interpret(context, debug)));
        BasicBlock block = compiledBlockStatement(context, "With", getBlock());
        return(context.executeWith(obj, block));
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }
    
    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();

        buf.append(indent).append("with (").append(this.expr.toString()).append(") {\n");
        buf.append(this.block.toIndentedString(indent + "  "));
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict);
    }
}
