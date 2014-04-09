package org.dynjs.ir;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.operands.Label;
import org.dynjs.runtime.JSProgram;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.PrintWriter;
import java.util.Map;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class IRByteCodeCompiler {
    public static JSProgram compile(final Scope scope, String fileName, boolean strict) {
        final int varOffset = 2;
        final Tuple<Instruction[], Map<Integer, Label[]>> tuple = scope.prepareForCompilation();
        final Instruction[] instructions = tuple.a;
        final Map<Integer, Label[]> jumpTable = tuple.b;
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        Object[] vars = new Object[scope.getLocalVariableSize()];

        final LabelNode[] labels = new LabelNode[instructions.length + 1];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new LabelNode();
        }

        System.out.println("VROGRAM:");
        int size = instructions.length;
        for (int i = 0; i < size; i++) {
            System.out.println("" + instructions[i]);
        }

        final JiteClass jiteClass = new JiteClass("org/dynjs/gen/" + "MEH");
        jiteClass.defineDefaultConstructor();
        CodeBlock block = new CodeBlock();
        for (int i = 0; i < instructions.length; i++) {
            final Instruction instruction = instructions[i];
            if (jumpTable.get(i) != null) {
                for (Label label : jumpTable.get(i)) {
//                    block = block.label(label);
                }
            }

            block = block.label(labels[i]);
            switch (instruction.getOperation()) {
                case ADD:
                    block = emitAdd(block, (Add) instruction);
                    break;
                case COPY:
                    block = emitCopy(block, (Copy) instruction);
                    break;
                case BEQ:
                    block = emitBEQ(block, (BEQ) instruction);

            }
        }
        block = block.aconst_null().areturn();

        jiteClass.defineMethod("execute", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, sig(Object.class), block);
        final byte[] bytes = jiteClass.toBytes();
        ClassReader reader = new ClassReader(bytes);
        CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));

        return null;
    }

    private static CodeBlock emitBEQ(CodeBlock block, BEQ instruction) {
        return block;
    }

    private static CodeBlock emitAdd(CodeBlock block, Add instruction) {
        return block;
    }

    private static CodeBlock emitCopy(CodeBlock block, Copy instruction) {
        return block;
    }
}
