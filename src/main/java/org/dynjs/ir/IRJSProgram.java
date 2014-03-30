/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.ir;

import java.util.List;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.Variable;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.Types;

public class IRJSProgram implements JSProgram {
    private Scope scope;
    private String filename;
    boolean isStrict;

    public IRJSProgram(Scope scope, String filename, boolean isStrict) {
        this.scope = scope;
        this.filename = filename;
        this.isStrict = isStrict;
    }
    @Override
    public Completion execute(ExecutionContext context) {
        Object result = Types.UNDEFINED;
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        Object[] vars = new Object[scope.getLocalVariableSize()];

        for (Instruction instr: scope.getInstructions()) {
            if (instr instanceof Copy) {
                Variable variable = ((Copy) instr).getResult();
                int offset = variable.getOffset();
                Object value = ((Copy) instr).getValue().retrieve(temps, vars);

                if (variable instanceof LocalVariable) {
                    vars[offset] = value;
                } else {
                    temps[offset] = value;
                }
            } else if (instr instanceof Return) {
                result = ((Return) instr).getValue().retrieve(temps, vars);
                break;
            }
        }

        return Completion.createNormal(result);
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public boolean isStrict() {
        return isStrict;
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return FunctionDeclaration.EMPTY_LIST;
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return VariableDeclaration.EMPTY_LIST;
    }
}
