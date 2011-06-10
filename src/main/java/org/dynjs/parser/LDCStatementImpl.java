/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dynjs.parser;

import org.antlr.runtime.tree.CommonTree;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 *
 * @author porcelli
 */
public class LDCStatementImpl implements Statement {

    private final LdcInsnNode node;
    private final CommonTree stringLiteral;

    public LDCStatementImpl(LdcInsnNode node, CommonTree stringLiteral) {
        this.node = node;
        this.stringLiteral = stringLiteral;
    }

    @Override
    public LdcInsnNode getStatement() {
        return node;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public InsnList getStatementList() {
        return null;
    }
}
