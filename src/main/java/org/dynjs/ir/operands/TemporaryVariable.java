package org.dynjs.ir.operands;

import org.dynjs.runtime.ExecutionContext;

public class TemporaryVariable extends OffsetVariable {
    public TemporaryVariable(int offset) {
        super(offset);
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
