package org.dynjs.ir;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.LT;
import org.dynjs.ir.operands.BooleanLiteral;
import org.dynjs.ir.operands.IntegerNumber;
import org.dynjs.ir.operands.Label;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.TemporaryVariable;
import org.dynjs.ir.operands.Variable;
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
    private final Scope scope;
    private final String fileName;
    private final boolean strict;
    private final List<BasicBlock> blockList;

    public IRByteCodeCompiler(Scope scope, String fileName, boolean strict) {
        this.scope = scope;
        this.fileName = fileName;
        this.strict = strict;
        this.blockList = scope.prepareForCompilation();
    }

    public JSProgram compile() {
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
                        emitAdd(block, (Add) instruction);
                        break;
                    case COPY:
                        emitCopy(block, (Copy) instruction);
                        break;
                    case BEQ:
                        emitBEQ(block, (BEQ) instruction, jumpMap);
                        break;
                    case LT:
                        emitLT(block, (LT) instruction);

                }
            }
        }
        block.aconst_null();
        block.areturn();

        jiteClass.defineMethod("execute", Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, sig(Object.class), block);
        final byte[] bytes = jiteClass.toBytes();
        ClassReader reader = new ClassReader(bytes);
        CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));

        return null;
    }

    private void emitLT(CodeBlock block, LT instruction) {
    }

    private void emitBEQ(CodeBlock block, BEQ instruction, Map<Label, LabelNode> jumpMap) {
        emitOperand(block, instruction.getArg1());
        emitOperand(block, instruction.getArg2());
        final Label label = instruction.getTarget();
        System.out.println("looking for label: " + label);
        final LabelNode node = jumpMap.get(label);
        block.if_icmpeq(node);
    }

    private void emitOperand(CodeBlock block, Operand operand) {
        switch (operand.getType()) {
            case INTEGER:
                emitInteger(block, (IntegerNumber) operand);
                break;
            case TEMP_VAR:
                emitTempVar(block, (TemporaryVariable) operand);
                break;
            case LOCAL_VAR:
                emitLocalVar(block, (LocalVariable) operand);
                break;
            case BOOLEAN:
                emitBoolean(block, (BooleanLiteral) operand);
        }
    }

    private void emitBoolean(CodeBlock block, BooleanLiteral operand) {
        block.pushBoolean((Boolean) operand.retrieve(null, null, null));
    }

    private void emitLocalVar(CodeBlock block, LocalVariable operand) {
        block.aload(getLocalVarOffset() + operand.getOffset());
    }

    private void emitTempVar(CodeBlock block, TemporaryVariable operand) {
        block.aload(getTempVarOffset() + operand.getOffset());
    }

    private int getTempVarOffset() {
        return scope.getLocalVariableSize();
    }

    private int getLocalVarOffset() {
        return 1;
    }

    private void emitInteger(CodeBlock block, IntegerNumber operand) {
        block.ldc(operand.getValue());
    }

    private void emitAdd(CodeBlock block, Add instruction) {

    }

    private void emitCopy(CodeBlock block, Copy instruction) {
        emitOperand(block, instruction.getValue());
        final Variable result = instruction.getResult();
        switch (result.getType()) {
            case TEMP_VAR:
                block.astore(getTempVarOffset() + ((TemporaryVariable) result).getOffset());
                break;
            case LOCAL_VAR:
                block.astore(getLocalVarOffset() + ((LocalVariable) result).getOffset());
                break;
        }
    }
}
