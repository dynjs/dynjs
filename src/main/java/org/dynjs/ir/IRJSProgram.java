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
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.OffsetVariable;
import org.dynjs.ir.operands.Variable;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.Types;
import org.jruby.dirgra.DirectedGraph;

public class IRJSProgram implements JSProgram {
    private Scope scope;
    private String filename;
    boolean isStrict;
    private Instruction[] instructions;

    public IRJSProgram(Scope scope, String filename, boolean isStrict) {
        this.scope = scope;
        this.filename = filename;
        this.isStrict = isStrict;
        this.instructions = scope.prepareForInterpret();

        System.out.println("PROGRAM:");
        int size = instructions.length;
        for (int i = 0; i < size; i++) {
            System.out.println("" + instructions[i]);
        }
    }
    @Override
    public Completion execute(ExecutionContext context) {
        Object result = Types.UNDEFINED;
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        Object[] vars = new Object[scope.getLocalVariableSize()];
        int size = instructions.length;

        int ipc = 0;
        while (ipc < size) {
            Instruction instr = instructions[ipc];
            ipc++;

            if (instr instanceof Copy) {
                Variable variable = ((Copy) instr).getResult();
                if (variable instanceof OffsetVariable) {
                    int offset = ((OffsetVariable) variable).getOffset();
                    Object value = ((Copy) instr).getValue().retrieve(temps, vars);

                    if (variable instanceof LocalVariable) {
                        vars[offset] = value;
                    } else {
                        temps[offset] = value;
                    }
                } else {
                    // FIXME: Lookup dynamicvariable
                }
            } else if (instr instanceof Jump) {
                ipc = ((Jump) instr).getTarget().getTargetIPC();
            } else if (instr instanceof BEQ) {
                BEQ beq = (BEQ) instr;
                Object arg1 = beq.getArg1().retrieve(temps, vars);
                Object arg2 = beq.getArg1().retrieve(temps, vars);

                if (arg1.equals(arg2)) {
                    ipc = beq.getTarget().getTargetIPC();
                }
            } else if (instr instanceof Return) {
                result = ((Return) instr).getValue().retrieve(temps, vars);
                break;
            }
        }

        System.out.println("RESULT is " + result);

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
