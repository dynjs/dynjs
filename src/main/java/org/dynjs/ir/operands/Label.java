package org.dynjs.ir.operands;

import java.util.List;
import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;

public class Label extends Operand {
    public static final Label UNRESCUED_REGION_LABEL = new Label("UNRESCUED_REGION", 0);

    public final String prefix;
    public final int id;
    private int targetIPC = -1;

    public Label(String prefix, int id) {
        super(OperandType.LABEL);
        this.prefix = prefix;
        this.id = id;
    }

    @Override
    public String toString() {
        return prefix + "_" + id;
    }

    public void addUsedVariables(List<Variable> l) {
    }

    public int getTargetIPC() {
        return targetIPC;
    }

    public void setTargetIPC(int targetIPC) {
        this.targetIPC = targetIPC;
    }
}
