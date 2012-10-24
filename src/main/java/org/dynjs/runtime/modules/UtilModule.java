package org.dynjs.runtime.modules;

import java.util.Arrays;
import java.util.Date;

import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

@Module(name = "util")
public class UtilModule {

    @Export
    public String format(Object... args) {
        if (args[0] instanceof String) {
            String format = (String) args[0];
            return String.format(format, Arrays.copyOfRange(args, 1, args.length));
        }
        else {
            String concat = "";
            for(Object arg: args) {
                concat += arg.toString() + " ";
            }
            return concat.trim();
        }
    }
    
    @Export
    public void debug(String message) {
        System.err.println(message);
    }
    
    @Export
    public void error(Object... args) {
        for (Object arg : args) {
            this.debug(arg.toString());
        }
    }

    @Export
    public void puts(Object... args) {
        for (Object arg : args) {
            System.out.println(arg.toString());
        }
    }

    @Export
    public void print(Object... args) {
        for (Object arg : args) {
            System.out.print(arg.toString());
        }
    }

    @Export
    public void log(String message) {
        System.out.println(new Date().toString() + " - " + message);
    }
    
    @Export
    public boolean isArray(Object thing) {
        return thing instanceof DynArray;
    }

    @Export
    public boolean isRegExp(Object thing) {
        return thing instanceof DynRegExp;
    }

    @Export
    public boolean isDate(Object thing) {
        return thing instanceof DynDate;
    }
    
    @Export
    public boolean isError(Object thing) {
        return thing instanceof DynObject && ((DynObject)thing).getClassName().equals("Error");
    }
    
    // TODO: util.inherits and util.inspect http://nodejs.org/api/util.html

}
