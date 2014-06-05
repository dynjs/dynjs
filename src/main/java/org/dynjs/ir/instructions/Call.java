package org.dynjs.ir.instructions;

import java.util.Map;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

public class Call extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand self;
    private Operand identifier;
    private Operand[] args;

    public Call(Variable result, Operand self, Operand identifier, Operand[] args) {
        super(Operation.CALL);
        this.result = result;
        this.self = self;
        this.identifier = identifier;
        this.args = args;
    }

    @Override
    public void updateResult(Variable newResult) {
        this.result = newResult;
    }

    public Operand[] getOperands() {
        Operand[] allArgs = new Operand[3 + args.length];

        allArgs[0] = result;
        allArgs[1] = self;
        allArgs[2] = identifier;

        System.arraycopy(args, 0, allArgs, 3, args.length);

        return allArgs;
    }

    public void simplifyOperands(Map<Operand, Operand> renameMap, boolean force) {
        self = self.getSimplifiedOperand(renameMap, force);
        identifier = identifier.getSimplifiedOperand(renameMap, force);

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].getSimplifiedOperand(renameMap, force);
        }
    }

    public Variable getResult() {
        return result;
    }

    public Operand getSelf() {
        return self;
    }

    public Operand getIdentifier() {
        return identifier;
    }

    public Operand[] getArgs() {
        return args;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append(result).append(" = call ");
        buf.append(self).append(", ");
        buf.append(identifier).append(", ");

        for (int i = 0; i < args.length-1; i++) {
            buf.append(args[i]).append(", ");
        }

        if (args.length != 0) {
            buf.append(args[args.length - 1]);
        }

        return buf.toString();
    }
}
