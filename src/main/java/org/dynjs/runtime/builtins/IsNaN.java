package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;

public class IsNaN implements Function {
    @Override
    public Object call(Object self, DynThreadContext context, Object... arguments) {
        if (arguments.length == 1) {
            if (isNullOrBooleanOrWhiteSpace(arguments[0].toString().trim())) { return false; }
            return (ParseInt.parseInt( arguments ).equals(Double.NaN));
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public String[] getParameters() {
        return new String[]{"a"};
    }
    
    private boolean isNullOrBooleanOrWhiteSpace(String value) {
        return (value.equals( "" ) || value.equals("null") || value.equals( "true" ) || value.equals( "false" ) );
    }
}
