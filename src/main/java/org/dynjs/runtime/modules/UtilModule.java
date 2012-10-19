package org.dynjs.runtime.modules;

import java.util.Arrays;

import org.dynjs.runtime.ExecutionContext;

@Module(name = "util")
public class UtilModule {

    @Export
    public String format(Object... args) {
        Object format = args[0];
        if (format instanceof String)
            return String.format(format.toString(), Arrays.copyOfRange(args, 1, args.length));
        else {
            String concat = "";
            for(Object arg: args) {
                concat += arg.toString() + " ";
            }
            return concat.trim();
        }
    }
    
    @Export
    public void debug(ExecutionContext context, Object self, String message) {
        System.err.println(message);
    }

}
