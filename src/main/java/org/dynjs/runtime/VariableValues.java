package org.dynjs.runtime;

/**
 * Naive storage for live values.
 */
public class VariableValues {
    private VariableValues parent;
    private Object[] vars;

    // FIXME: We can hide this behind static method and create specialized types of this,
    // but then it should be an interface or abstract class
    public VariableValues(int size, VariableValues parent) {
        this.parent = parent;

        vars = new Object[size];
    }

    public VariableValues getParent() {
        return parent;
    }

    public void setVars(Object[] vars) {
        this.vars = vars;
    }

    public Object getVar(int offset, int depth) {
        if (depth == 0) return vars[offset];

        return getParent().getVar(offset, depth - 1);
    }

    public void setVar(int offset, int depth, Object value) {
        if (depth == 0) {
            vars[offset] = value;
            return;
        }

        getParent().setVar(offset, depth - 1, value);
    }
}
