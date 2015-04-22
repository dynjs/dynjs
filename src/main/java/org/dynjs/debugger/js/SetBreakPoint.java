package org.dynjs.debugger.js;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.model.ScriptBreakpoint;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;

/**
 * @author Bob McWhirter
 */
public class SetBreakPoint extends AbstractNonConstructorFunction {

    private final Debugger debugger;

    public SetBreakPoint(GlobalContext context, Debugger debugger) {
        super(context);
        this.debugger = debugger;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (args.length == 0) {
            throw new ThrowException(context, context.createError("Error", "Parameters have wrong types."));
        }


        if (!(args[0] instanceof JSFunction)) {
            throw new ThrowException(context, context.createError("Error", "Parameters have wrong types."));
        }

        String fileName = ((JSFunction) args[0]).getFileName();

        long line = 0;
        if (args.length >= 2) {
            line = (long) args[1];
        }

        long column = 0;
        if (args.length >= 3) {
            column = (long) args[1];
        }

        ScriptBreakpoint breakpoint = new ScriptBreakpoint( fileName, (int) line, (int) column );
        this.debugger.setBreakpoint( breakpoint );
        return breakpoint.getNumber();
    }
}
