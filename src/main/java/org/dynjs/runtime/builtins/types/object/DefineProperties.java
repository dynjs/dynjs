package org.dynjs.runtime.builtins.types.object;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class DefineProperties extends AbstractNativeFunction {

    public DefineProperties(GlobalContext globalContext) {
        super(globalContext, "o", "properties");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.7
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }
        
        JSObject jsObj = (JSObject) o;
        JSObject props = Types.toObject( context, args[1] );
        
        List<String> names = props.getOwnEnumerablePropertyNames().toList();
        List<PropertyDescriptor> descriptors = new ArrayList<>();
        
        for ( String name : names ) {
            Object attrs = props.get( context, name );
            descriptors.add( PropertyDescriptor.toPropertyDescriptor(context, attrs));
        }
        
        int len = names.size();
        
        for ( int i = 0 ; i < len ; ++i ) {
            jsObj.defineOwnProperty(context, names.get(i), descriptors.get(i), true);
        }
        
        return jsObj;
    }
}
