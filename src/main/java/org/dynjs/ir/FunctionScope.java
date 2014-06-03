package org.dynjs.ir;

/**
 */
public class FunctionScope extends Scope {
    private String[] parameterNames;
    private String name;
    private String syntheticMethodName;
    private String syntheticSignature;

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

    public void setSyntheticMethodName(String syntheticMethodName) {
        this.syntheticMethodName = syntheticMethodName;
    }

    public String getSyntheticMethodName() {
        return syntheticMethodName;
    }

    public void setSyntheticSignature(String syntheticSignature) {
        this.syntheticSignature = syntheticSignature;
    }

    public String getSyntheticSignature() {
        return syntheticSignature;
    }
}
