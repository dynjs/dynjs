package org.dynjs.ir.operands;

import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

public class TemporaryVariable extends OffsetVariable {
    public TemporaryVariable(int offset) {
        super(OperandType.TEMP_VAR, offset);
    }

    @Override
    public String getName() {
        return "%t_" + getOffset();
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return temps[getOffset()];
    }
}
