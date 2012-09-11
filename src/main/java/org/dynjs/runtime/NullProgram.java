package org.dynjs.runtime;


public class NullProgram extends BaseProgram {
    private String filename;

    public NullProgram(String filename) {
        super(null);
        this.filename = filename;
    }

    @Override
    public Completion execute(ExecutionContext context) {
        return Completion.createNormal();
    }

    @Override
    public String getFileName() {
        return this.filename;
    }
    

}
