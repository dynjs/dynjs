package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public class Label extends Operand {
    public static final Label UNRESCUED_REGION_LABEL = new Label("UNRESCUED_REGION", 0);

    public final String prefix;
    public final int id;
    private int targetIPC = -1;

    public Label(String prefix, int id) {
        this.prefix = prefix;
        this.id = id;
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return null;
    }

    @Override
    public String toString() {
        return prefix + "_" + id;
    }

    public int getTargetIPC() {
        return targetIPC;
    }

    public void setTargetIPC(int targetIPC) {
        this.targetIPC = targetIPC;
    }
}
