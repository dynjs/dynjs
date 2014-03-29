package org.dynjs.ir.operands;

/**
 * Local variable in the current scope.
 * Offset is an identifier for the JIT and a location
 * identifier for the Interpreter.
 */
public class LocalVariable extends Variable {
    private String name;
    private int offset;

    public LocalVariable(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String getName() {
        return name;
    }
}
