package org.dynjs.runtime.modules;

import java.io.OutputStream;
import java.io.PrintStream;

@Module(name = "console")
public class ConsoleModule {

    @Export
    public void log(Object self, DynThreadContext context, String message) {
        OutputStream errStream = context.getErrorStream();
        
        PrintStream err = null;
        
        if ( errStream instanceof PrintStream ) {
            err = (PrintStream) errStream;
        } else {
            err = new PrintStream( errStream );
        }
        
        err.println( message );
        
    }

}
