package org.dynjs.runtime.modules;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.StackElement;

@Module(name = "console")
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
        context.collectStackElements(stack);
        boolean first = true;
        for (StackElement each : stack) {
            if ( first ) {
                first = false;
                continue;
            }
            log(context, self, each.toString() );
        }
    }

}
