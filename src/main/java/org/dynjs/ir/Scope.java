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
import org.dynjs.ir.representations.BasicBlock;
import org.dynjs.ir.representations.CFG;
import org.dynjs.ir.representations.CFGLinearizer;
import org.jruby.dirgra.DirectedGraph;

// FIXME: Modelled as single scope now but I doubt this will hold for long.
public class Scope {
    private Scope parent;

    private Map<Integer, Variable> temporaryVariables = new HashMap<>();
    private int temporaryVariablesIndex = 0;

    private Map<String, Variable> localVariables = new HashMap<>();
    // What next variable index will be (also happens to be current size
    private int localVariablesIndex = 0;

    private List<Instruction> instructions = new ArrayList<>();

    private Map<String, Integer> nextVarIndex = new HashMap<>();

    private HashMap<Integer, Integer> rescueMap;

    private boolean isStrict;

    public Scope(Scope parent, boolean isStrict) {
        this.parent = parent;
        this.isStrict = isStrict;
    }

    public Instruction addInstruction(Instruction instruction) {
        instructions.add(instruction);

        return instruction;
    }

    public boolean isStrict() {
        return isStrict;
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


    public Instruction[] prepareForInterpret() {
        final CFG cfg = new CFG(this);
        final DirectedGraph<BasicBlock> graph = cfg.build(getInstructions());

        // FIXME: Add debug config for this
        System.out.println(cfg.toStringInstrs());
        System.out.println(graph.toString());

        // FIXME: We don't save this linearized set yet
        return prepareIPCs(CFGLinearizer.linearize(cfg), cfg);
    }

    public List<BasicBlock> prepareForCompilation() {
        final CFG cfg = new CFG(this);
        cfg.build(getInstructions());
        return CFGLinearizer.linearize(cfg);
    }

    private static Label[] catLabels(Label[] labels, Label cat) {
        if (labels == null) return new Label[] {cat};
        Label[] newLabels = new Label[labels.length + 1];
        System.arraycopy(labels, 0, newLabels, 0, labels.length);
        newLabels[labels.length] = cat;
        return newLabels;
    }

    private Instruction[] prepareIPCs(List<BasicBlock> list, CFG cfg) {
        // Set up IPCs
        List<Instruction> newInstrs = new ArrayList<Instruction>();
        HashMap<Label, Integer> labelIPCMap = new HashMap<Label, Integer>();
        int ipc = 0;
        for (BasicBlock basicBlock: list) {
            Label label = basicBlock.getLabel();
            labelIPCMap.put(label, ipc);
            // This assumes if multiple equal/same labels exist which are scattered around the scope
            // must be the same Java instance or only this one will get a targetPC set.
            label.setTargetIPC(ipc);
            List<Instruction> bbInstrs = basicBlock.getInstructions();
            int bbInstrsLength = bbInstrs.size();
            for (int i = 0; i < bbInstrsLength; i++) {
                Instruction instr = bbInstrs.get(i);

                newInstrs.add(instr);
                instr.setIPC(ipc);
                ipc++;
            }
        }

        cfg.getExitBB().getLabel().setTargetIPC(ipc + 1);  // Exit BasicBlock IPC


        setupRescueMap(list, cfg); // Set up the rescue map

        return newInstrs.toArray(new Instruction[newInstrs.size()]);
    }

    public void setupRescueMap(List<BasicBlock> list, CFG cfg) {
        rescueMap = new HashMap<Integer, Integer>();
        for (BasicBlock basicBlock : list) {
            BasicBlock rescuerBB = cfg.getRescuerBBFor(basicBlock);
            int rescuerPC = (rescuerBB == null) ? -1 : rescuerBB.getLabel().getTargetIPC();
            for (Instruction instruction : basicBlock.getInstructions()) {
                rescueMap.put(instruction.getIPC(), rescuerPC);
            }
        }
    }
}
