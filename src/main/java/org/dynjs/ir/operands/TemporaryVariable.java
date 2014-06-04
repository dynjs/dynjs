package org.dynjs.ir.operands;

import java.util.List;
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
    public Object retrieve(ExecutionContext context, Object[] temps) {
        try {
            return temps[getOffset()];
        } catch (Exception e) {
            System.out.println("Error: Temporary Variable '" + getName() + "' cannot be retrieved.");
            throw e;
        }
    }
}
