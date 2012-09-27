package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;

public class Match extends AbstractNativeFunction {

    public Match(GlobalObject globalObject) {
        super(globalObject, "regexp");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.10
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        JSObject rx = null;
        if (args[0] instanceof JSObject && ((JSObject) args[0]).getClassName().equals("RegExp")) {
            rx = (JSObject) args[0];
        } else {
            rx = BuiltinRegExp.newRegExp(context, Types.toString( context, args[0]), null );
        }

        Object global = rx.get(context, "global");

        JSFunction execFn = (JSFunction) context.getPrototypeFor("RegExp").get(context, "exec");
        if (global != Boolean.TRUE) {
            return context.call(execFn, rx, s);
        }

        rx.put(context, "lastIndex", 0, false);

        JSObject a = BuiltinArray.newArray(context);
        long previousLastIndex = 0;
        long n = 0;
        boolean lastMatch = true;

        while (lastMatch) {
            Object result = context.call(execFn, rx, s);
            if (result == Types.NULL) {
                lastMatch = false;
            } else {
                long thisIndex = (long) rx.get(context, "lastIndex");
                if (thisIndex == previousLastIndex) {
                    rx.put(context, "lastIndex", thisIndex + 1, false);
                    previousLastIndex = thisIndex + 1;
                } else {
                    previousLastIndex = thisIndex;
                }

                final Object matchStr = ((JSObject) result).get(context, "0");

                a.defineOwnProperty(context, "" + n, new PropertyDescriptor() {
                    {
                        set("Value", matchStr);
                        set("Writable", true);
                        set("Configurable", true);
                        set("Enumerable", true);
                    }
                }, false);
                
                ++n;
            }
        }
        
        if ( n == 0 ) {
            return Types.NULL;
        }
        
        return a;

    }
}
