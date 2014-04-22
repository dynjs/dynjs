package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;

public class Identity extends AbstractNativeFunction {

    public Identity(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) self;

        StringBuffer details = new StringBuffer();

        details.append( "[" );

        Object ctorProp = jsObj.getProperty(context, "__ctor__");

        if ( ctorProp instanceof PropertyDescriptor ) {
            details.append( ((PropertyDescriptor) ctorProp).getValue() );
        } else {
            details.append( "unknown" );
        }

        details.append( "@" );
        details.append( System.identityHashCode( jsObj ) );
        details.append( "]" );

        return details.toString();
    }

}
