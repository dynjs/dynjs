package org.dynjs.compiler;

import me.qmx.jitescript.CodeBlock;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

public class CodeBlockUtils {

    public static CodeBlock offset(CodeBlock block, int offset) {

        InsnList list = block.getInstructionList();
        int len = list.size();

        for (int i = 0; i < len; ++i) {
            AbstractInsnNode each = list.get( i );
            if (each instanceof VarInsnNode) {
                VarInsnNode node = (VarInsnNode) each;
                if (node.var > 3) {
                    node.var = node.var + offset;
                }
            }
        }

        return block;
    }

}
