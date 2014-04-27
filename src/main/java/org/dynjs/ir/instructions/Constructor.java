package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Created by enebo on 4/27/14.
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
}
