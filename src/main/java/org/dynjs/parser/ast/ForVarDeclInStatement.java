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
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public class ForVarDeclInStatement extends AbstractForInStatement {

    private final VariableDeclaration decl;
    private final CallSite get;

    public ForVarDeclInStatement(Position position, VariableDeclaration decl, Expression rhs, Statement block) {
        super(position, rhs, block);
        this.decl = decl;
        this.get = DynJSBootstrapper.factory().createGet( rhs.getPosition() );
    }

    public VariableDeclaration getDeclaration() {
        return this.decl;
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<VariableDeclaration>(super.getVariableDeclarations());
        decls.add(decl);
        return decls;
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("for (").append(decl).append(" in ").append(getRhs().toString()).append(") {\n");
        buf.append(getBlock().toIndentedString(indent + "  "));
        buf.append(indent).append("}");
        return buf.toString();
    }
    
    public int getSizeMetric() {
        return super.getSizeMetric() + decl.getSizeMetric();
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public Completion interpret(ExecutionContext context, boolean debug) {
        String varName = (String) getDeclaration().interpret(context, debug);
        Object exprRef = getRhs().interpret(context, debug);
        Object exprValue = getValue(this.get, context, exprRef);

        if (exprValue == Types.NULL || exprValue == Types.UNDEFINED) {
            return(Completion.createNormal());

        }

        JSObject obj = Types.toObject(context, exprValue);

        Object v = null;

        List<String> names = obj.getAllEnumerablePropertyNames().toList();

        for (String each : names) {
            Reference varRef = context.resolve(varName);

            varRef.putValue(context, each);

            Completion completion = (Completion) getBlock().interpret(context, debug);
            //Completion completion = invokeCompiledBlockStatement(context, "ForVarDeclsIn", statement.getBlock());

            if (completion.value != null) {
                v = completion.value;
            }

            if (completion.type == Completion.Type.BREAK) {
                if (completion.target == null || getLabels().contains(completion.target)) {
                    return(Completion.createNormal(v));
                } else {
                    return(completion);
                }

            }

            if (completion.type == Completion.Type.RETURN || completion.type == Completion.Type.BREAK) {
                return(completion);

            }
        }

        return(Completion.createNormal(v));
    }

}
