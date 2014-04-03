package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.operands.Variable;

public class Call extends Instruction {
    private Variable result;
    private Operand self;
    private Operand identifier;
    private Operand[] args;

    public Call(Variable result, Operand self, Operand identifier, Operand[] args) {
        this.result = result;
        this.self = self;
        this.identifier = identifier;
        this.args = args;
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
