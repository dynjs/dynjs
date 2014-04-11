package org.dynjs.ir;

/**
 * Created by enebo on 4/11/14.
 */
public class FunctionScope extends Scope {
    private String[] parameterNames;
    private String filename;

    public FunctionScope(Scope parent, boolean isStrict, String[] parameterNames, String filename) {
        super(parent, isStrict);

        this.parameterNames = parameterNames;
        this.filename = filename;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public String getFilename() {
        return filename;
    }
}
