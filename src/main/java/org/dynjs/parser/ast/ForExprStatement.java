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
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class ForExprStatement extends AbstractForStatement {

    private final Expression initialize;
    private final CallSite testGet;
    private final CallSite incrGet;

    public ForExprStatement(Position position, Expression initialize, Expression test, Expression increment, Statement block) {
        super(position, test, increment, block);
        this.initialize = initialize;
        if (test != null) {
            this.testGet = DynJSBootstrapper.factory().createGet(test.getPosition());
        } else {
            this.testGet = null;
        }
        if (increment != null) {
            this.incrGet = DynJSBootstrapper.factory().createGet(increment.getPosition());
        } else {
            this.incrGet = null;
        }
    }

    public Expression getExpr() {
        return this.initialize;
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("for (").append(this.initialize == null ? "" : this.initialize.toString()).append(" ; ");
        buf.append((getTest() == null ? "" : getTest().toString())).append(" ; ");
        buf.append((getIncrement() == null ? "" : getIncrement().toString())).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public int getSizeMetric() {
        if (this.initialize != null) {
            return this.initialize.getSizeMetric() + super.getSizeMetric();
        }
        return super.getSizeMetric();
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        if (getExpr() != null) {
            getExpr().interpret(context, debug);
        }

        Expression test = getTest();
        Expression incr = getIncrement();
        Statement body = getBlock();

        Object v = null;

        while (true) {
            if (test != null) {

                if (!Types.toBoolean(getValue(this.testGet, context, test.interpret(context, debug)))) {
                    break;
                }
            }

            Completion completion = (Completion) body.interpret(context, debug);
            //Completion completion = invokeCompiledBlockStatement(context, "ForExpr", body);

            if (completion.value != null && completion.value != Types.UNDEFINED) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || getLabels().contains(completion.target)) {
                    return (Completion.createNormal(v));
                } else {
                    completion.value = v;
                    return (completion);
                }

            }
            if (completion.type == Completion.Type.RETURN) {
                return (completion);

            }
            if (completion.type == Completion.Type.CONTINUE) {
                if (completion.target != null && !getLabels().contains(completion.target)) {
                    return (completion);

                }
            }

            if (incr != null) {
                getValue(this.incrGet, context, incr.interpret(context, debug));
            }
        }

        return (Completion.createNormal(v));
    }
}
