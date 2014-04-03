package org.dynjs.ir.representations;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.instructions.Branch;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.operands.Undefined;
import org.dynjs.ir.representations.CFG.EdgeType;

/**
 * This produces a linear list of BasicBlocks so that the linearized instruction
 * list is in executable form.  In generating this list, we will also add jumps
 * where required and remove as many jumps as possible.
 *
 * Ordinary BasicBlocks will follow FollowThrough edges and just concatenate
 * together eliminating the need for executing a jump instruction during
 * execution.
 *
 * Notes:
 * 1. Basic blocks ending in branches have two edges (FollowTrough/NotTaken and Taken)
 * 2. All BasicBlocks can possibly have two additional edges related to exceptions:
 *    - one that transfers control to a rescue block (if one exists that protects
 *      the excepting instruction) which is also responsible for running ensures
 *    - one that transfers control to an ensure block (if one exists) for
 *      situations where we bypass the rescue block (breaks and thread-kill).
 * 3. Branch, Jump, Return, and Exceptions are all boundaries for BasicBlocks
 * 4. Dummy Entry and Exit BasicBlocks exist in all CFGs
 */
public class CFGLinearizer {
    public static List<BasicBlock> linearize(CFG cfg) {
        List<BasicBlock> list = new ArrayList<BasicBlock>();
        BitSet processed = new BitSet(cfg.size()); // Assumes all id's are used

        linearizeInner(cfg, list, processed, cfg.getEntryBB());
        verifyAllBasicBlocksProcessed(cfg, processed);
        fixupList(cfg, list);

        return list;
    }

    private static void linearizeInner(CFG cfg, List<BasicBlock> list,
                                       BitSet processed, BasicBlock current) {
        if (processed.get(current.getID())) return;

        // Cannot lay out current block till its fall-through predecessor has been laid out already
        BasicBlock source = cfg.getIncomingSourceOfType(current, EdgeType.FALL_THROUGH);
        if (source != null && !processed.get(source.getID())) return;

        list.add(current);
        processed.set(current.getID());

        // First, fall-through BB
        BasicBlock fallThrough = cfg.getOutgoingDestinationOfType(current, EdgeType.FALL_THROUGH);
        if (fallThrough != null) linearizeInner(cfg, list, processed, fallThrough);

        // Next, regular edges
        for (BasicBlock destination: cfg.getOutgoingDestinationsOfType(current, EdgeType.REGULAR)) {
            linearizeInner(cfg, list, processed, destination);
        }

        // Next, exception edges
        for (BasicBlock destination: cfg.getOutgoingDestinationsOfType(current, EdgeType.EXCEPTION)) {
            linearizeInner(cfg, list, processed, destination);
        }

        // Next, exit
        for (BasicBlock destination: cfg.getOutgoingDestinationsOfType(current, EdgeType.EXIT)) {
            linearizeInner(cfg, list, processed, destination);
        }
    }

    /**
     * Process (fixup) list of instruction and add or remove jumps.
     */
    private static void fixupList(CFG cfg, List<BasicBlock> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            BasicBlock current = list.get(i);

            if (current.isExitBB()) { // exit not last
                current.addInstr(new Return(Undefined.UNDEFINED));
                continue;
            }

            Instruction lastInstr = current.getLastInstr();
            if (lastInstr instanceof Jump) { // if jumping to next BB then remove it
                tryAndRemoveUnneededJump(list.get(i + 1), cfg, lastInstr, current);
            } else {
                addJumpIfNextNotDestination(cfg, list.get(i + 1), lastInstr, current);
            }
        }

        BasicBlock current = list.get(n - 1);
        if (!current.isExitBB()) {
            Instruction lastInstr = current.getLastInstr();
            // Last instruction of the last basic block in the linearized list can NEVER
            // be a branch instruction because this basic block would then have a fallthrough
            // which would have to be present after it.
            assert (!(lastInstr instanceof Branch));

            if ((lastInstr == null) || !lastInstr.transfersControl()) {
                // We are guaranteed to have at least one non-exception edge because
                // the exit BB post-dominates all BBs in the CFG even when exception
                // edges are removed.
                //
                // Verify that we have exactly one non-exception target
                Iterator<BasicBlock> iter = cfg.getOutgoingDestinationsNotOfType(current, EdgeType.EXCEPTION).iterator();
                BasicBlock target = iter.next();
                assert (target != null && !iter.hasNext());

                // System.out.println("BB " + curr.getID() + " is the last bb in the layout! Adding a jump to " + tgt._label);
                current.addInstr(new Jump(target.getLabel()));
            }
        }
    }

    private static void tryAndRemoveUnneededJump(BasicBlock next, CFG cfg, Instruction lastInstr, BasicBlock current) {
        if (next == cfg.getBBForLabel(((Jump) lastInstr).getTarget())) current.removeInstr(lastInstr);
    }

    // If there is no jump at add of block and the next block is not destination insert a valid jump
    private static void addJumpIfNextNotDestination(CFG cfg, BasicBlock next, Instruction lastInstr, BasicBlock current) {
        Iterator<BasicBlock> outs = cfg.getOutgoingDestinations(current).iterator();
        BasicBlock target = outs.hasNext() ? outs.next() : null;

        if (target != null && !outs.hasNext()) {
            if ((target != next) && ((lastInstr == null) || !lastInstr.transfersControl())) {
                current.addInstr(new Jump(target.getLabel()));
            }
        }
    }

    private static void verifyAllBasicBlocksProcessed(CFG cfg, BitSet processed) throws RuntimeException {
        // Verify that all bbs have been laid out!
        for (BasicBlock b : cfg.getBasicBlocks()) {
            if (!processed.get(b.getID())) {
                throw new RuntimeException("Bad CFG linearization: BB " + b.getID() + " has been missed!");
            }
        }
    }
}

