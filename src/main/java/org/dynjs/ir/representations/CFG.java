package org.dynjs.ir.representations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.jruby.dirgra.Edge;

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

    public CFG(Scope scope) {
        this.scope = scope;
        this.graph = new DirectedGraph<BasicBlock>();
        this.bbMap = new HashMap<Label, BasicBlock>();
        this.rescuerMap = new HashMap<BasicBlock, BasicBlock>();
        this.nextBBId = 0;
        this.entryBB = this.exitBB = null;
        this.globalEnsureBB = null;
        this.postOrderList = null;
    }

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

        buildExitBasicBlock(nestedExceptionRegions, firstBB, returnBBs, exceptionBBs, nextBBIsFallThrough, currBB, entryBB);

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

    /**
     * Create special empty exit BasicBlock that all BasicBlocks will eventually
     * flow into.  All Edges to this 'dummy' BasicBlock will get marked with
     * an edge type of EXIT.
     *
     * Special BasicBlocks worth noting:
     * 1. Exceptions, Returns, Entry(why?) -> ExitBB
     * 2. Returns -> ExitBB
     */
    private BasicBlock buildExitBasicBlock(Stack<ExceptionRegion> nestedExceptionRegions, BasicBlock firstBB,
                                           List<BasicBlock> returnBBs, List<BasicBlock> exceptionBBs, boolean nextIsFallThrough, BasicBlock currBB, BasicBlock entryBB) {
        exitBB = createBB(nestedExceptionRegions);

        graph.addEdge(entryBB, exitBB, EdgeType.EXIT);
        graph.addEdge(entryBB, firstBB, EdgeType.FALL_THROUGH);

        for (BasicBlock rb : returnBBs) {
            graph.addEdge(rb, exitBB, EdgeType.EXIT);
        }

        for (BasicBlock rb : exceptionBBs) {
            graph.addEdge(rb, exitBB, EdgeType.EXIT);
        }

        if (nextIsFallThrough) graph.addEdge(currBB, exitBB, EdgeType.EXIT);

        return exitBB;
    }

    public void collapseStraightLineBBs() {
        // Collect cfgs in a list first since the cfg/graph API returns an iterator
        // over live data.  But, basic block merging modifies that live data.
        //
        // SSS FIXME: So, we need a cfg/graph API that returns an iterator over
        // frozen data rather than live data.
        List<BasicBlock> cfgBBs = new ArrayList<BasicBlock>();
        for (BasicBlock b: getBasicBlocks()) cfgBBs.add(b);

        Set<BasicBlock> mergedBBs = new HashSet<BasicBlock>();
        for (BasicBlock b: cfgBBs) {
            if (!mergedBBs.contains(b) && (outDegree(b) == 1)) {
                for (Edge<BasicBlock> e : getOutgoingEdges(b)) {
                    BasicBlock outB = e.getDestination().getData();
                    if ((e.getType() != EdgeType.EXCEPTION) && (inDegree(outB) == 1) && (mergeBBs(b, outB) == true)) {
                        mergedBBs.add(outB);
                    }
                }
            }
        }
    }

    private void deleteOrphanedBlocks(DirectedGraph<BasicBlock> graph) {
        // System.out.println("\nGraph:\n" + toStringGraph());
        // System.out.println("\nInstructions:\n" + toStringInstrs());

        // FIXME: Quick and dirty implementation
        while (true) {
            BasicBlock bbToRemove = null;
            for (BasicBlock b : graph.allData()) {
                if (b == entryBB) continue; // Skip entry bb!

                // Every other bb should have at least one incoming edge
                if (graph.findVertexFor(b).getIncomingEdges().isEmpty()) {
                    bbToRemove = b;
                    break;
                }
            }
            if (bbToRemove == null) break;

            removeBB(bbToRemove);
        }
    }

    public Collection<BasicBlock> getBasicBlocks() {
        return graph.allData();
    }

    public Set<Edge<BasicBlock>> getOutgoingEdges(BasicBlock block) {
        return graph.findVertexFor(block).getOutgoingEdges();
    }

    public BasicBlock getRescuerBBFor(BasicBlock block) {
        return rescuerMap.get(block);
    }

    public int inDegree(BasicBlock b) {
        return graph.findVertexFor(b).inDegree();
    }

    private boolean mergeBBs(BasicBlock a, BasicBlock b) {
        BasicBlock aR = getRescuerBBFor(a);
        BasicBlock bR = getRescuerBBFor(b);
        // We can merge 'a' and 'b' if one of the following is true:
        // 1. 'a' and 'b' are both not empty
        //    They are protected by the same rescue block.
        //    NOTE: We need not check the ensure block map because all ensure blocks are already
        //    captured in the bb rescue block map.  So, if aR == bR, it is guaranteed that the
        //    ensure blocks for the two are identical.
        // 2. One of 'a' or 'b' is empty.  We dont need to check for rescue block match because
        //    an empty basic block cannot raise an exception, can it?
        if ((aR == bR) || a.isEmpty() || b.isEmpty()) {
            // First, remove straight-line jump, if present
            Instruction lastInstr = a.getLastInstr();
            if (lastInstr instanceof Jump) a.removeInstr(lastInstr);

            // Swallow b's instrs.
            a.swallowBB(b);

            // Fixup edges
            graph.removeEdge(a, b);
            for (Edge<BasicBlock> e : getOutgoingEdges(b)) {
                addEdge(a, e.getDestination().getData(), e.getType());
            }

            // Delete bb
            removeBB(b);

            // Update rescue map
            if (aR == null && bR != null) {
                setRescuerBB(a, bR);
            }

            return true;
        } else {
            return false;
        }
    }

    private void optimize() {
        List<Edge> toRemove = new ArrayList<Edge>();

        for (BasicBlock b : graph.allData()) {
            boolean noExceptions = true;
            for (Instruction i : b.getInstructions()) {
                if (i.canRaiseException()) {
                    noExceptions = false;
                    break;
                }
            }

            if (noExceptions) {
                for (Edge<BasicBlock> e : graph.findVertexFor(b).getOutgoingEdgesOfType(EdgeType.EXCEPTION)) {
                    BasicBlock source = e.getSource().getData();
                    BasicBlock destination = e.getDestination().getData();
                    toRemove.add(e);

                    if (rescuerMap.get(source) == destination) rescuerMap.remove(source);
                }
            }
        }

        if (!toRemove.isEmpty()) {
            for (Edge edge: toRemove) {
                graph.removeEdge(edge);
            }
        }

        deleteOrphanedBlocks(graph);

        collapseStraightLineBBs();
    }

    public int outDegree(BasicBlock b) {
        return graph.findVertexFor(b).outDegree();
    }

    public void removeBB(BasicBlock b) {
        graph.removeVertexFor(b);
        bbMap.remove(b.getLabel());
        rescuerMap.remove(b);
    }

    public void setRescuerBB(BasicBlock block, BasicBlock rescuerBlock) {
        rescuerMap.put(block, rescuerBlock);
    }
}
