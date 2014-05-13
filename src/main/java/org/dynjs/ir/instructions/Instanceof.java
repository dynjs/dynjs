package org.dynjs.ir.instructions;

import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Created by enebo on 5/5/14.
 */
public class Instanceof extends BinaryInstr {

    public Instanceof(Variable result, Operand lhs, Operand rhs) {
        super(Operation.INSTANCEOF, result, lhs, rhs);
    }

    @Override
    public String toString() {
        return "" + getResult() + " = " + getLHS() + " instanceof " + getRHS();
    }
}
