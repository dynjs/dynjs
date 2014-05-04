package org.dynjs.ir;

/**
 */
public class FunctionScope extends Scope {
    private String[] parameterNames;
    private String name;

    public FunctionScope(Scope parent, String fileName, boolean isStrict, String[] parameterNames, String name) {
        super(parent, fileName, isStrict);

        this.parameterNames = parameterNames;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }
}
