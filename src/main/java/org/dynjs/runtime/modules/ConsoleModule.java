package org.dynjs.runtime.modules;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.builtins.types.error.StackElement;

@Module(name = "dyn_console")
public class ConsoleModule {

    @Export
    public void log(ExecutionContext context, Object self, String message) {
        OutputStream errStream = context.getConfig().getErrorStream();

        PrintStream err = null;

        if (errStream instanceof PrintStream) {
            err = (PrintStream) errStream;
        } else {
            err = new PrintStream(errStream);
        }

        err.println(message);
    }

    @Export
    public void trace(ExecutionContext context, Object self) {
        List<StackElement> stack = new ArrayList<>();
        context.getParent().collectStackElements(stack);
        for (StackElement each : stack) {
            log(context, self, each.toString() );
        }
    }
}
