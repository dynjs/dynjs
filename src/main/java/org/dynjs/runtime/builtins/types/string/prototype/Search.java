package org.dynjs.runtime.builtins.types.string.prototype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;

public class Search extends AbstractNativeFunction {

    public Search(GlobalObject globalObject) {
        super(globalObject, "regexp");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.12
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        
        JSObject rx = null;
        if ( args[0] instanceof JSObject && ((JSObject)args[0]).getClassName().equals( "RegExp")) {
            rx = (JSObject) args[0];
        } else {
            rx = BuiltinRegExp.newRegExp(context, Types.toString( context, args[0]), null );
        }
        
        String source = (String) rx.get(context, "source");
        Pattern pattern = Pattern.compile( source );
        Matcher matcher = pattern.matcher(s);
        
        if ( matcher.find() ) {
            return (long) matcher.start();
        }
        
        return -1L;
    }

}
