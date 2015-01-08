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
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class SwitchStatement extends BaseStatement {

    private Expression expr;
    private final CallSite valueGet;
    private List<CaseClause> caseClauses;
    private List<CallSite> caseGets;

    public SwitchStatement(Position position, Expression expr, List<CaseClause> caseClauses) {
        super(position);
        this.expr = expr;
        this.valueGet = DynJSBootstrapper.factory().createGet(expr.getPosition());
        this.caseClauses = caseClauses;
        this.caseGets = new ArrayList<>();
        for (CaseClause each : this.caseClauses) {
            this.caseGets.add(DynJSBootstrapper.factory().createGet(each.getPosition()));
        }
    }

    public Expression getExpr() {
        return this.expr;
    }

    public List<CaseClause> getCaseClauses() {
        return this.caseClauses;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        for (CaseClause each : caseClauses) {
            decls.addAll(each.getVariableDeclarations());
        }
        return decls;
    }

    public int getSizeMetric() {
        int size = this.expr.getSizeMetric();

        for (CaseClause each : caseClauses) {
            size += each.getSizeMetric();
        }

        return size + 11;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        Object value = getValue(this.valueGet, context, getExpr().interpret(context, debug));
        Object v = null;

        int numClauses = getCaseClauses().size();

        int startIndex = -1;
        int defaultIndex = -1;

        for (int i = 0; i < numClauses; ++i) {
            CaseClause each = this.caseClauses.get(i);
            CallSite eachGet = this.caseGets.get(i);
            if (each instanceof DefaultCaseClause) {
                defaultIndex = i;
                continue;
            }

            Object caseTest = each.getExpression().interpret(context, debug);
            if (Types.compareStrictEquality(context, value, getValue(eachGet, context, caseTest))) {
                startIndex = i;
                break;
            }
        }

        if (startIndex < 0 && defaultIndex >= 0) {
            startIndex = defaultIndex;
        }

        if (startIndex >= 0) {
            for (int i = startIndex; i < numClauses; ++i) {
                CaseClause each = getCaseClauses().get(i);
                if (each.getBlock() != null) {
                    Completion completion = (Completion) each.getBlock().interpret(context, debug);
                    v = completion.value;

                    if (completion.type == Completion.Type.BREAK) {
                        break;
                    } else if (completion.type == Completion.Type.CONTINUE) {
                        return (completion);
                    } else if (completion.type == Completion.Type.RETURN) {
                        return (completion);

                    }
                }
            }
        }

        return (Completion.createNormal(v));

    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();

        buf.append(indent).append("switch (").append(expr.toString()).append(" ) {\n");
        for (CaseClause each : caseClauses) {
            buf.append(each.toIndentedString("  " + indent));
        }
        buf.append(indent).append("}");

        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

}
