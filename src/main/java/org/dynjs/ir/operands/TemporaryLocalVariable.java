package org.dynjs.ir.operands;

/**
 * Created by enebo on 6/4/14.
 */
public class TemporaryLocalVariable extends TemporaryVariable {
    private String name;

    public TemporaryLocalVariable(String name, int offset) {
        super(offset);

        this.name = name;
    }

    @Override
    public String getName() {
        return "%t_" + name + "_" + getOffset();
    }
}
