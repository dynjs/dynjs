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

public class ForVarDeclStatement extends AbstractForStatement {

    private final List<VariableDeclaration> declList;
    private final CallSite testGet;
    private final CallSite incrGet;

    public ForVarDeclStatement(Position position, List<VariableDeclaration> declList, Expression test, Expression increment, Statement block) {
        super(position, test, increment, block);
        this.declList = declList;

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

    public List<VariableDeclaration> getDeclarationList() {
        return this.declList;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        decls.addAll(declList);
        decls.addAll(super.getVariableDeclarations());
        return decls;
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("for (").append(this.declList == null ? "" : this.declList).append(" ; ");
        buf.append((getTest() == null ? "" : getTest().toString())).append(" ; ");
        buf.append((getIncrement() == null ? "" : getIncrement().toString())).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }

    public int getSizeMetric() {
        int size = super.getSizeMetric();

        for (VariableDeclaration each : this.declList) {
            size += each.getSizeMetric();
        }

        return size;
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public Completion interpret(ExecutionContext context, boolean debug) {
        List<VariableDeclaration> decls = getDeclarationList();
        for (VariableDeclaration each : decls) {
            each.interpret(context, debug);
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
            //Completion completion = invokeCompiledBlockStatement(context, "ForVarDecl", body);

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
