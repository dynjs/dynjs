package org.dynjs.runtime;


public class NullProgram extends BaseProgram {
    public static final NullProgram INSTANCE = new NullProgram();

    public NullProgram() {
        super(null);
    }

    @Override
    public Completion execute(ExecutionContext context) {
        return Completion.createNormal();
    }

}
