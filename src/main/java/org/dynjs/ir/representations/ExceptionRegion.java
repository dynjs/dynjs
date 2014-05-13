package org.dynjs.ir.representations;

import java.util.ArrayList;
import java.util.List;
import org.dynjs.ir.operands.Label;

public class ExceptionRegion {
    private Label firstRescueBlockLabel; // LabelInstr of the first rescue block

    private List<BasicBlock> exclusiveBBs;  // Basic blocks exclusively contained within this region
    private List<ExceptionRegion> nestedRegions; // Rescue regions nested within this one
    private BasicBlock endBB;         // Last BB of the rescued region

    public ExceptionRegion(Label firstRescueBlockLabel, BasicBlock startBB) {
        this.firstRescueBlockLabel = firstRescueBlockLabel;
        exclusiveBBs = new ArrayList<BasicBlock>();
        nestedRegions = new ArrayList<ExceptionRegion>();
    }

    public void setEndBB(BasicBlock bb) {
        endBB = bb;
    }

    public List<BasicBlock> getExclusiveBBs() {
        return exclusiveBBs;
    }

    public void addBB(BasicBlock bb) {
        exclusiveBBs.add(bb);
    }

    public void addNestedRegion(ExceptionRegion r) {
        nestedRegions.add(r);
        exclusiveBBs.remove(r.exclusiveBBs.get(0));
    }

    public Label getFirstRescueBlockLabel() {
        return firstRescueBlockLabel;
    }
}
