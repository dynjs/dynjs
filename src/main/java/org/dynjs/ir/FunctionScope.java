package org.dynjs.ir;

/**
 */
public class FunctionScope extends Scope {
    private String[] parameterNames;

    public FunctionScope(Scope parent, String fileName, boolean isStrict, String[] parameterNames) {
        super(parent, fileName, isStrict);

        this.parameterNames = parameterNames;
    }


    public String[] getParameterNames() {
        return parameterNames;
    }
}
