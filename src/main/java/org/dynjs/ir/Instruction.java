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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dynjs.ir.instructions.ResultInstruction;
import org.dynjs.ir.operands.Variable;

public abstract class Instruction {
    private final Operation operation;
    // Mutable state to make it easier for jumping around
    private int ipc = -1;

    public Instruction(Operation operation) {
        this.operation = operation;
    }

    public int getIPC() {
        return ipc;
    }

    public void setIPC(int ipc) {
        this.ipc = ipc;
    }

    /**
     *  List of all variables used by this instruction.
     */
    public List<Variable> getUsedVariables() {
        ArrayList<Variable> vars = new ArrayList<Variable>();

        for (Operand o : getOperands()) {
            o.addUsedVariables(vars);
        }

        return vars;
    }

    public void simplifyOperands(Map<Operand, Operand> renameMap, boolean force) {
    }

    public void renameVariables(Map<Operand, Operand> renameMap) {
        simplifyOperands(renameMap, true);

        if (this instanceof ResultInstruction) {
            ResultInstruction resultInstruction = (ResultInstruction) this;

            Variable oldVariable = resultInstruction.getResult();
            Variable newVariable = (Variable) renameMap.get(oldVariable);

            if (newVariable != null) resultInstruction.updateResult(newVariable);
        }
    }

    public abstract Operand[] getOperands();

    /**
     * This instruction can set or hint or some useful information onto the scope it belongs
     * to.
     *
     * @param scope is where this instruciton lives
     * @return true if scope had information added otherwise return false
     */
    public boolean computeScopeFlags(Scope scope) {
        return false;
    }

    // jump/branch/return/exception (e.g. can jump out of current instr list)
    public boolean transfersControl() {
        return false;
    }

    /**
     * Can this instruction potentially raise a JS Exception?
     */
    public boolean canRaiseException() {
        return false;
    }

    /**
     * Dump out this instruction in a string frield format for debugging purposes.
     */
    public String dump(String indent) {
        return indent + getClass().getSimpleName();
    }

    public Operation getOperation() {
        return operation;
    }
}
