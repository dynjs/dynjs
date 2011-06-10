/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dynjs.parser;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 *
 * @author porcelli
 */
public class BlockStatementImpl implements Statement {
    private final InsnList block;

    public BlockStatementImpl(InsnList block) {
        this.block = block;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public AbstractInsnNode getStatement() {
        return null;
    }

    @Override
    public InsnList getStatementList() {
        return block;
    }
    
}
