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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dynjs.ir.operands.Label;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.TemporaryVariable;
import org.dynjs.ir.operands.Variable;

// FIXME: Modelled as single scope now but I doubt this will hold for long.
public class Scope {
    private Scope parent;

    private Map<Integer, Variable> temporaryVariables = new HashMap<>();
    private int temporaryVariablesIndex = 0;

    private Map<String, Variable> localVariables = new HashMap<>();
    // What next variable index will be (also happens to be current size
    private int localVariablesIndex = 0;

    private List<Instruction> instructions = new ArrayList<>();

    private Map<String, Integer> nextVarIndex;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Instruction addInstruction(Instruction instruction) {
        instructions.add(instruction);

        return instruction;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Tries to find a variable or returns null if it cannot.  This
     * will walk all scopes to find a captured variable.
     */
    public Variable findVariable(String name) {
        Variable variable = localVariables.get(name);

        if (variable != null) {
            return variable;
        }

        if (parent != null) {
            return parent.findVariable(name);
        }

        return null;
    }

    public int getLocalVariableSize() {
        return localVariablesIndex;
    }

    /**
     * Return an existing variable or return a new one made in this scope.
     */
    public Variable acquireLocalVariable(String name) {
        Variable variable = findVariable(name);

        if (variable == null) {
            variable = new LocalVariable(this, name, localVariablesIndex);
            localVariables.put(name, variable);
            localVariablesIndex++;
        }

        return variable;
    }

    // FIXME: Do I care about all the boxing here of index?
    public Variable acquireTemporaryVariable(int index) {
        Variable variable = temporaryVariables.get(index);

        return variable == null ? createTemporaryVariable() : variable;
    }

    public Variable createTemporaryVariable() {
        Variable variable = new TemporaryVariable(temporaryVariablesIndex);

        temporaryVariablesIndex++;

        return variable;
    }

    public int getTemporaryVariableSize() {
        return temporaryVariablesIndex;
    }

    protected int getPrefixCountSize(String prefix) {
        Integer index = nextVarIndex.get(prefix);

        if (index == null) return 0;

        return index.intValue();
    }

    protected int allocateNextPrefixedName(String prefix) {
        int index = getPrefixCountSize(prefix);

        nextVarIndex.put(prefix, index + 1);

        return index;
    }

    public Label getNewLabel(String prefix) {
        return new Label(prefix, allocateNextPrefixedName(prefix));
    }

    public Label getNewLabel() {
        return getNewLabel("LBL");
    }
}
