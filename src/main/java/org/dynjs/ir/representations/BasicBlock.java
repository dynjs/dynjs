package org.dynjs.ir.representations;

import java.util.ArrayList;
import java.util.List;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.operands.Label;
import org.jruby.dirgra.ExplicitVertexID;

public class BasicBlock implements ExplicitVertexID, Comparable {
    private int id;                     // ID of Basic Block
    private CFG cfg;                    // CFG for this BasicBlock
    private Label label;                // All basic blocks have a starting label
    private List<Instruction> instructions;   // List of non-label instructions
    private boolean isRescueEntry;      // Is this basic block entry of a rescue?
    private Instruction[] instructionsArray;

    public BasicBlock(CFG cfg, Label label) {
        this.label = label;
        this.cfg = cfg;
        this.id = cfg.getNextBBID();
        isRescueEntry = false;
        initInstrs();
    }

    private void initInstrs() {
        instructions = new ArrayList<>();
        instructionsArray = null;
    }

    @Override
    public int getID() {
        return id;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean isEntryBB() {
        return cfg.getEntryBB() == this;
    }

    public boolean isExitBB() {
        return cfg.getExitBB() == this;
    }

    public void markRescueEntryBB() {
        this.isRescueEntry = true;
    }

    public boolean isRescueEntry() {
        return this.isRescueEntry;
    }

    public void addInstr(Instruction i) {
        instructions.add(i);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public int instrCount() {
        return instructions.size();
    }

    public Instruction[] getInstructionsArray() {
        if (instructionsArray == null) instructionsArray = instructions.toArray(new Instruction[instructions.size()]);

        return instructionsArray;
    }

    public Instruction getLastInstr() {
        int n = instructions.size();
        return (n == 0) ? null : instructions.get(n-1);
    }

    public boolean removeInstr(Instruction i) {
        return i == null? false : instructions.remove(i);
    }

    public boolean isEmpty() {
        return instructions.isEmpty();
    }

    @Override
    public int compareTo(Object o) {
        BasicBlock other = (BasicBlock) o;

        if (id == other.id) return 0;
        if (id < other.id) return -1;

        return 1;
    }

    public void swallowBB(BasicBlock foodBB) {
        instructions.addAll(foodBB.getInstructions());
    }

    @Override
    public String toString() {
        return "BB [" + id + ":" + label + "]";
    }

    public String toStringInstrs() {
        StringBuilder buf = new StringBuilder(toString() + "\n");

        for (Instruction instr : getInstructions()) {
            buf.append('\t').append(instr).append('\n');
        }

        return buf.toString();
    }

    public String dump(String indent) {
        StringBuilder buf = new StringBuilder(indent + toString() + "\n");

        for (Instruction instr : getInstructions()) {
            buf.append(instr.dump(indent + "  ")).append('\n');
        }

        return buf.toString();
    }
}
