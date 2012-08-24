package org.dynjs.runtime;

import java.util.List;

public class StackGetter extends AbstractNativeFunction {

    private String errorName;
    private String message;
    private List<StackElement> stack;

    public StackGetter(GlobalObject globalObject, String errorName, String message, List<StackElement> stack) {
        super(globalObject, false);
        this.errorName = errorName;
        this.message = message;
        this.stack = stack;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        StringBuffer buf = new StringBuffer();
        if (this.errorName == null) {
            buf.append("<unknown>");
        } else {
            buf.append(this.errorName);
        }
        if (this.message != null && !this.message.equals("")) {
            buf.append(": " + this.message);
        }
        buf.append("\n");
        for (StackElement each : stack) {
            buf.append("  at " + each + "\n");
        }
        return buf.toString();
    }

}
