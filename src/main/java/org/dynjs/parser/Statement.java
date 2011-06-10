package org.dynjs.parser;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public interface Statement {

    boolean isList();

    AbstractInsnNode getStatement();

    InsnList getStatementList();
}
