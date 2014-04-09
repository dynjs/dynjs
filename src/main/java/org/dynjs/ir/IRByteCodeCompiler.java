package org.dynjs.ir;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
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
        jiteClass.defineMethod("execute", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, sig(Object.class), new CodeBlock() {{
            for (int i = 0; i < instructions.length; i++) {
                final Instruction instruction = instructions[i];
                switch (instruction.getOperation()) {
                    case ADD: break;
                    case COPY: break;

                }
            }
            for (Instruction instruction : instructions) {
//                System.out.println(instruction.getClass());
//                if (instruction instanceof LabelInstr) {
//                    final Label label = ((LabelInstr) instruction).getLabel();
////                    label(new LabelNode());
//                } else if (instruction instanceof Copy) {
//                    Copy copy = (Copy) instruction;
//                    final Variable variable = copy.getResult();
//                    final Operand value = copy.getValue();
//                    if (value instanceof IntegerNumber) {
//                        pushInt((int) ((IntegerNumber) value).getValue());
//                        istore(((LocalVariable) variable).getOffset() + varOffset);
//                    }
//                } else if (instruction instanceof Add) {
////                    iadd();
//                    nop();
//                } else if (instruction instanceof BEQ) {
//                }
            }
            aconst_null();
            areturn();
        }});
        final byte[] bytes = jiteClass.toBytes();
        ClassReader reader = new ClassReader(bytes);
        CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));

        return null;
    }
}
