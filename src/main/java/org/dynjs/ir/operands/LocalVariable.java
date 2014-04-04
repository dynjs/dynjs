package org.dynjs.ir.operands;

import org.dynjs.ir.Scope;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

/**
 * Offset is an identifier for the JIT and a location
 * identifier for the Interpreter.
 */
public class LocalVariable extends OffsetVariable {
    private String name;

    public LocalVariable(Scope scope, String name, int offset) {
        super(offset);

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return vars[getOffset()];
    }
}
