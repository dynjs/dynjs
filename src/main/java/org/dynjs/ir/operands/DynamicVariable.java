package org.dynjs.ir.operands;

import java.util.List;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

/**
 * A variable in which we do not know it's location.
 */
public class DynamicVariable extends Variable {
    private String name;

    public DynamicVariable(String name) {
        super(OperandType.DYNAMIC_VAR);
        this.name = name;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return context.resolve(name).getValue(context);
    }

    @Override
    public String getName() {
        return name;
    }
}
