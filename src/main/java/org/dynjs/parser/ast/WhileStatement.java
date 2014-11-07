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
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class WhileStatement extends AbstractIteratingStatement {

    private final CallSite testGet;

    private final Expression vbool;
    private final Statement vloop;

    public WhileStatement(Position position, Expression vbool, Statement vloop) {
        super(position);
        this.vbool = vbool;
        this.vloop = vloop;
        this.testGet = DynJSBootstrapper.factory().createGet( vbool.getPosition() );
    }

    public Expression getTest() {
        return this.vbool;
    }

    public Statement getBlock() {
        return this.vloop;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.vloop.getVariableDeclarations();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.vloop.getFunctionDeclarations();
    }

    public int getSizeMetric() {
        return vbool.getSizeMetric() + 7;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        Expression testExpr = getTest();
        Statement block = getBlock();

        Object v = null;

        while (true) {
            Boolean testResult = Types.toBoolean(getValue(this.testGet, context, testExpr.interpret(context, debug)));
            if (testResult) {
                // block.accept(context, this, strict);
                // Completion completion = (Completion) pop();
                Completion completion = invokeCompiledBlockStatement(context, "While", block);
                if (completion.value != null) {
                    v = completion.value;
                }
                if (completion.type == Completion.Type.CONTINUE) {
                    if (completion.target == null) {
                        continue;
                    } else if (!getLabels().contains(completion.target)) {
                        return(completion);

                    } else {
                        continue;
                    }
                }
                if (completion.type == Completion.Type.BREAK) {
                    if (completion.target == null) {
                        break;
                    } else if (!getLabels().contains(completion.target)) {
                        return(completion);

                    } else {
                        break;
                    }

                }
                if (completion.type == Completion.Type.RETURN) {
                    return(Completion.createReturn(v));

                }
            } else {
                break;
            }
        }

        return(Completion.createNormal(v));
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();

        buf.append(indent).append("while (").append(this.vbool.toString()).append(") {\n");
        buf.append(this.vloop.toIndentedString(indent + "  "));
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }
}
