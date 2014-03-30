package org.dynjs.ir.operands;

import org.dynjs.runtime.Types;

public class TemporaryVariable extends Variable {
    public TemporaryVariable(int offset) {
        super(offset);
    }

    @Override
    public String getName() {
        return "%t_" + getOffset();
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return vars[getOffset()];
    }
}
