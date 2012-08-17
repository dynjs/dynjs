package org.dynjs.runtime.modules;

import java.io.OutputStream;
import java.io.PrintStream;

import org.dynjs.runtime.ExecutionContext;

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

}
