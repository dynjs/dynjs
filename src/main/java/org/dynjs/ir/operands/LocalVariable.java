package org.dynjs.ir.operands;

import org.dynjs.ir.OperandType;
import org.dynjs.ir.Scope;
import org.dynjs.runtime.ExecutionContext;

/**
 * Offset is an identifier for the JIT and a location
 * identifier for the Interpreter.
 */
public class LocalVariable extends OffsetVariable {
    private String name;
    private int depth;

    public LocalVariable(String name, int offset, int depth) {
        super(OperandType.LOCAL_VAR, offset);
        this.name = name;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        try {
            return context.getVars().getVar(getOffset(), getDepth());
        } catch (Exception e) {
            System.out.println("Error: Local Variable '" + name + "' cannot be retrieved.");
            throw e;
        }
    }

    @Override
    public String toString() {
        return getName() + "{" + getOffset() + ", " + getDepth() + "}";
    }
}
