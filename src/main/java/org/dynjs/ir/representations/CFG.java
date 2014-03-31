package org.dynjs.ir.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Scope;
import org.dynjs.ir.instructions.Branch;
import org.dynjs.ir.instructions.ExceptionRegionEndMarker;
import org.dynjs.ir.instructions.ExceptionRegionStartMarker;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.LabelInstr;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.instructions.ThrowException;
import org.dynjs.ir.operands.Label;
import org.jruby.dirgra.DirectedGraph;

/**
  */
public class CFG {
    public enum EdgeType {
        REGULAR,       // Any non-special edge.  Not really used.
        EXCEPTION,     // Edge to exception handling basic blocks
        FALL_THROUGH,  // Edge which is the natural fall through choice on a branch
        EXIT           // Edge to dummy exit BB
    }

    private Scope scope;
    private Map<Label, BasicBlock> bbMap;

    // Map of bb -> first bb of the rescue block that initiates exception handling for all exceptions thrown within this bb
    private Map<BasicBlock, BasicBlock> rescuerMap;
    private BasicBlock entryBB;
    private BasicBlock exitBB;

    /**
     * BB that traps all exception-edges out of the CFG.  frame popping & clean up loc.
     */
    private BasicBlock globalEnsureBB;

    private DirectedGraph<BasicBlock> graph;

    private int nextBBId;       // Next available basic block id

    LinkedList<BasicBlock> postOrderList; // Post order traversal list of the cfg

    public int getNextBBID() {
        nextBBId++;
        return nextBBId;
    }

    public BasicBlock getEntryBB() {
        return entryBB;
    }

    public BasicBlock getExitBB() {
        return exitBB;
    }

    /**
     *  Build the Control Flow Graph
     */
    public DirectedGraph<BasicBlock> build(List<Instruction> instructions) {
        // Map of label & basic blocks which are waiting for a bb with that label
        Map<Label, List<BasicBlock>> forwardRefs = new HashMap<Label, List<BasicBlock>>();

        // List of bbs that have a 'return' instruction
        List<BasicBlock> returnBBs = new ArrayList<BasicBlock>();

        // List of bbs that have a 'throw' instruction
        List<BasicBlock> exceptionBBs = new ArrayList<BasicBlock>();

        // Stack of nested rescue regions
        Stack<ExceptionRegion> nestedExceptionRegions = new Stack<ExceptionRegion>();

        // List of all rescued regions
        List<ExceptionRegion> allExceptionRegions = new ArrayList<ExceptionRegion>();

        // Dummy entry basic block (see note at end to see why)
        entryBB = createBB(nestedExceptionRegions);

        // First real bb
        BasicBlock firstBB = createBB(nestedExceptionRegions);

        // Build the rest!
        BasicBlock currBB = firstBB;
        BasicBlock newBB;
        boolean bbEnded = false;
        boolean nextBBIsFallThrough = true;
        for (Instruction i : instructions) {
            if (i instanceof LabelInstr) {
                Label l = ((LabelInstr) i).getLabel();
                newBB = createBB(l, nestedExceptionRegions);

                // Jump instruction bbs dont add an edge to the succeeding bb by default
                if (nextBBIsFallThrough) graph.addEdge(currBB, newBB, EdgeType.FALL_THROUGH);
                currBB = newBB;
                bbEnded = false;
                nextBBIsFallThrough = true;

                // Add forward reference edges
                List<BasicBlock> frefs = forwardRefs.get(l);
                if (frefs != null) {
                    for (BasicBlock b : frefs) {
                        graph.addEdge(b, newBB, EdgeType.REGULAR);
                    }
                }
            } else if (bbEnded && i instanceof ExceptionRegionEndMarker) {
                newBB = createBB(nestedExceptionRegions);
                // Jump instruction bbs dont add an edge to the succeeding bb by default
                if (nextBBIsFallThrough) graph.addEdge(currBB, newBB, EdgeType.FALL_THROUGH); // currBB cannot be null!
                currBB = newBB;
                bbEnded = false;
                nextBBIsFallThrough = true;
            }

            if (i instanceof ExceptionRegionStartMarker) {
                // We dont need the instruction anymore -- so it is not added to the CFG.
                ExceptionRegionStartMarker ersmi = (ExceptionRegionStartMarker) i;
                ExceptionRegion rr = new ExceptionRegion(ersmi.getLabel(), currBB);
                rr.addBB(currBB);
                allExceptionRegions.add(rr);

                if (!nestedExceptionRegions.empty()) {
                    nestedExceptionRegions.peek().addNestedRegion(rr);
                }

                nestedExceptionRegions.push(rr);
            } else if (i instanceof ExceptionRegionEndMarker) {
                // We dont need the instruction anymore -- so it is not added to the CFG.
                nestedExceptionRegions.pop().setEndBB(currBB);
            } else if (i.transfersControl()) {
                bbEnded = true;
                currBB.addInstr(i);
                Label tgt;
                nextBBIsFallThrough = false;
                if (i instanceof Branch) {
                    tgt = ((Branch) i).getTarget();
                    nextBBIsFallThrough = true;
                } else if (i instanceof Jump) {
                    tgt = ((Jump) i).getTarget();
                } else if (i instanceof Return) {
                    tgt = null;
                    returnBBs.add(currBB);
                } else if (i instanceof ThrowException) {
                    tgt = null;
                    exceptionBBs.add(currBB);
                } else {
                    throw new RuntimeException("Unhandled case in CFG builder for basic block ending instr: " + i);
                }

                if (tgt != null) addEdge(currBB, tgt, forwardRefs);
            } else if (!(i instanceof LabelInstr)) {
                currBB.addInstr(i);
            }
        }

        // Process all rescued regions
        for (ExceptionRegion rr : allExceptionRegions) {
            // When this exception region represents an unrescued region
            // from a copied ensure block, we have a dummy label
            Label rescueLabel = rr.getFirstRescueBlockLabel();
            if (!Label.UNRESCUED_REGION_LABEL.equals(rescueLabel)) {
                BasicBlock firstRescueBB = bbMap.get(rescueLabel);
                // Mark the BB as a rescue entry BB
                firstRescueBB.markRescueEntryBB();

                // Record a mapping from the region's exclusive basic blocks to the first bb that will start exception handling for all their exceptions.
                // Add an exception edge from every exclusive bb of the region to firstRescueBB
                for (BasicBlock b : rr.getExclusiveBBs()) {
                    setRescuerBB(b, firstRescueBB);
                    graph.addEdge(b, firstRescueBB, EdgeType.EXCEPTION);
                }
            }
        }

        return graph;
    }

    private BasicBlock createBB(Stack<ExceptionRegion> nestedExceptionRegions) {
        return createBB(scope.getNewLabel(), nestedExceptionRegions);
    }

    private BasicBlock createBB(Label label, Stack<ExceptionRegion> nestedExceptionRegions) {
        BasicBlock basicBlock = new BasicBlock(this, label);
        addBasicBlock(basicBlock);

        if (!nestedExceptionRegions.empty()) nestedExceptionRegions.peek().addBB(basicBlock);

        return basicBlock;
    }

    public void addBasicBlock(BasicBlock bb) {
        graph.findOrCreateVertexFor(bb); // adds vertex to graph
        bbMap.put(bb.getLabel(), bb);

        // Reset so later dataflow analyses get all basic blocks
        postOrderList = null;
    }

    public Scope getScope() {
        return scope;
    }

    public void addEdge(BasicBlock source, BasicBlock destination, Object type) {
        graph.findOrCreateVertexFor(source).addEdgeTo(destination, type);
    }

    private void addEdge(BasicBlock src, Label targetLabel, Map<Label, List<BasicBlock>> forwardRefs) {
        BasicBlock target = bbMap.get(targetLabel);

        if (target != null) {
            graph.addEdge(src, target, EdgeType.REGULAR);
            return;
        }

        // Add a forward reference from target -> source
        List<BasicBlock> forwardReferences = forwardRefs.get(targetLabel);

        if (forwardReferences == null) {
            forwardReferences = new ArrayList<BasicBlock>();
            forwardRefs.put(targetLabel, forwardReferences);
        }

        forwardReferences.add(src);
    }

    public void setRescuerBB(BasicBlock block, BasicBlock rescuerBlock) {
        rescuerMap.put(block, rescuerBlock);
    }
}
