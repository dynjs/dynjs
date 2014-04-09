package org.dynjs.ir;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.operands.Label;
import org.dynjs.ir.representations.BasicBlock;
import org.dynjs.runtime.JSProgram;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class IRByteCodeCompiler {
    public static JSProgram compile(final Scope scope, String fileName, boolean strict) {
        final int varOffset = 2;
        final List<BasicBlock> blockList = scope.prepareForCompilation();
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        Object[] vars = new Object[scope.getLocalVariableSize()];
        final HashMap<Label, LabelNode> jumpMap = new HashMap<>();


        System.out.println("VROGRAM:");

        final JiteClass jiteClass = new JiteClass("org/dynjs/gen/" + "MEH");
        jiteClass.defineDefaultConstructor();
        CodeBlock block = new CodeBlock();
        // first pass for gathering labels
        for (BasicBlock bb : blockList) {
            final Label label = bb.getLabel();
            System.out.println("label: " + label);
            final LabelNode labelNode = new LabelNode();
            jumpMap.put(label, labelNode);
        }

        // second pass for emitting
        for (BasicBlock bb : blockList) {
            block.label(jumpMap.get(bb.getLabel()));

            for (Instruction instruction : bb.getInstructions()) {
                System.out.println(instruction);
                switch (instruction.getOperation()) {
                    case ADD:
                        block = emitAdd(block, (Add) instruction);
                        break;
                    case COPY:
                        block = emitCopy(block, (Copy) instruction);
                        break;
                    case BEQ:
                        block = emitBEQ(block, (BEQ) instruction, jumpMap);
                }
            }
        }
        block = block.aconst_null().areturn();

        jiteClass.defineMethod("execute", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, sig(Object.class), block);
        final byte[] bytes = jiteClass.toBytes();
        ClassReader reader = new ClassReader(bytes);
        CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));

        return null;
    }

    private static CodeBlock emitBEQ(CodeBlock block, BEQ instruction, Map<Label, LabelNode> jumpMap) {
        final Label label = instruction.getTarget();
        System.out.println("looking for label: " + label);
        final LabelNode node = jumpMap.get(label);
        block.if_icmpeq(node);
        return block;
    }

    private static CodeBlock emitAdd(CodeBlock block, Add instruction) {
        return block;
    }

    private static CodeBlock emitCopy(CodeBlock block, Copy instruction) {
        return block;
    }
}
