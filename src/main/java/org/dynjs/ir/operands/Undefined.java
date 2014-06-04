package org.dynjs.ir.operands;

import java.util.List;
import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

/**
 * Created by enebo on 3/29/14.
 */
public class Undefined extends Operand {
    public static final Operand UNDEFINED = new Undefined();

    protected Undefined() {
        super(OperandType.UNDEFINED);
    }

    public void addUsedVariables(List<Variable> l) {
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return Types.UNDEFINED;
    }

    public String toString() {
        return "%undefined";
    }
}
