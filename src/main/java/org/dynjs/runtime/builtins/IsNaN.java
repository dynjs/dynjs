package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class IsNaN extends AbstractNativeFunction {

    public IsNaN(GlobalObject globalObject) {
        super( globalObject, "text" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            return isNaN( args );
        } else {
            return Types.UNDEFINED;
        }
    }

    static boolean isNaN(Object... args) {
        Object arg = args[0];
        if (arg.equals( Double.NaN )) {
            return true;
        } else if (isNullOrBooleanOrWhiteSpace( arg )) {
            return false;
        }
        int radix = ParseInt.extractRadix( (String) arg );
        String text = ParseInt.cleanText( (String) arg, radix );
        return (ParseInt.parseInt( text, radix ).equals( Double.NaN ));
    }

    static boolean isNullOrBooleanOrWhiteSpace(Object arg) {
        if (arg == Types.NULL || arg.getClass() == Boolean.class) {
            return true;
        } else if (arg.getClass() == String.class) {
            String value = ((String) arg).trim();
            return value.equals( "" );
        }
        return false;
    }

}
