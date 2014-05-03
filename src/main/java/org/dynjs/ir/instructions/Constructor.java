package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Construct a new object (e.g. 'new Array();').
 */
public class Constructor extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand identifier;
    private Operand[] args;

    public Constructor(Variable result, Operand identifier, Operand[] args) {
        super(Operation.CONSTRUCTOR);

        this.result = result;
        this.identifier = identifier;
        this.args = args;
    }

    public Operand getIdentifier() {
        return identifier;
    }

    public Operand[] getArgs() {
        return args;
    }

    public Variable getResult() {
        return result;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("new " + identifier + "(");

        int count = args.length - 1;
        for (int i = 0; i < count; i++) {
            buf.append(args[i]).append(", ");
        }

        if (args.length != 0) {
            buf.append(args[args.length - 1]);
        }

        buf.append(")");

        return buf.toString();
    }
}
