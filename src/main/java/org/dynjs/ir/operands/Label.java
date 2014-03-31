package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public class Label extends Operand {
    public static final Label UNRESCUED_REGION_LABEL = new Label("UNRESCUED_REGION", 0);

    public final String prefix;
    public final int id;

    public Label(String prefix, int id) {
        this.prefix = prefix;
        this.id = id;
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return null;
    }
}
