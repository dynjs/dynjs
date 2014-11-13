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
import java.util.ArrayList;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class IfStatement extends BaseStatement {

    private final Expression testExpr;
    private final Statement thenBlock;
    private final Statement elseBlock;

    private final CallSite testGet;

    public IfStatement(Position position, final Expression testExpr, final Statement thenBlock, final Statement elseBlock) {
        super( position );
        this.testExpr = testExpr;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;

        this.testGet = DynJSBootstrapper.factory().createGet( testExpr.getPosition() );
    }

    public Expression getTest() {
        return this.testExpr;
    }

    public Statement getThenBlock() {
        return this.thenBlock;
    }

    public Statement getElseBlock() {
        return this.elseBlock;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        if (this.thenBlock != null) {
            decls.addAll(this.thenBlock.getVariableDeclarations());
        }
        if (this.elseBlock != null) {
            decls.addAll(this.elseBlock.getVariableDeclarations());
        }
        return decls;
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();

        buf.append(indent).append("if (").append(this.testExpr.toString()).append(") {\n");
        buf.append(this.thenBlock.toIndentedString(indent + "  "));
        if (this.elseBlock != null) {
            buf.append(indent).append("} else {\n").append(this.elseBlock.toIndentedString(indent + "  "));
        }
        buf.append(indent).append("}");

        return buf.toString();
    }

    public String dump(String indent) {
        StringBuilder buf = new StringBuilder(super.dump(indent));

        buf.append(testExpr.dump(indent + "  "));
        buf.append(thenBlock.dump(indent + "  "));
        if (elseBlock != null) {
            buf.append(elseBlock.dump(indent + "  "));
        }

        return buf.toString();
    }
    
    public int getSizeMetric() {
        return this.testExpr.getSizeMetric() + 7;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public Completion interpret(ExecutionContext context, boolean debug) {
        Boolean result = Types.toBoolean(getValue(this.testGet, context, getTest().interpret(context, debug)));

        if (result) {
            return(invokeCompiledBlockStatement(context, "Then", getThenBlock()));
        } else if (getElseBlock() != null) {
            return(invokeCompiledBlockStatement(context, "Else", getElseBlock()));
        } else {
            return(Completion.createNormal());
        }
    }

}
