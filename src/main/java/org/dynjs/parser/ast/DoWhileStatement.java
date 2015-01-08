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

public class DoWhileStatement extends AbstractIteratingStatement {

    private final Expression test;
    private final Statement block;

    private final CallSite testGet;

    public DoWhileStatement(Position position, final Statement block, final Expression test) {
        super(position);
        this.block = block;
        this.test = test;
        this.testGet = DynJSBootstrapper.factory().createGet(test.getPosition());
    }

    public Expression getTest() {
        return this.test;
    }

    public Statement getBlock() {
        return this.block;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.block.getFunctionDeclarations();
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public int getSizeMetric() {
        return test.getSizeMetric() + 5;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        Expression testExpr = getTest();
        Statement block = getBlock();

        Object v = null;

        while (true) {
            Completion completion = invokeCompiledBlockStatement(context, "DoWhile", block);
            if (completion.value != null) {
                v = completion.value;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target == null) {
                    // nothing
                } else if (!getLabels().contains(completion.target)) {
                    return (completion);

                }
            } else if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null) {
                    break;
                } else if (!getLabels().contains(completion.target)) {
                    return (completion);

                } else {
                    break;
                }
            } else if (completion.type == Completion.Type.RETURN) {
                return (Completion.createReturn(v));

            }


            Boolean testResult = Types.toBoolean(getValue(this.testGet, context, testExpr.interpret(context, debug)));
            if (!testResult) {
                break;
            }
        }

        return (Completion.createNormal(v));
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("do {\n");
        buf.append(indent).append("} while (").append(test.toString()).append(")");
        return buf.toString();
    }
}
