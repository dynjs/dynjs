package org.dynjs.runtime.modules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dynjs.api.Function;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynThreadContext;

public class JavaFunction implements Function {

    public JavaFunction(Object object, Method method) {
        this.object = object;
        this.method = method;
    }
    
    @Override
    public Object call(Object self, DynThreadContext context, Object... args) {
        // TODO: don't worry about DynThreadContext for no-arg methods
        // be smarter in general.
        
        if ( (args.length+1) != this.method.getParameterTypes().length ) {
            throw new DynJSException( args.length + " arguments provided, " + this.method.getParameterTypes().length + " expected" );
        }
        
        Object[] newArgs = new Object[ args.length + 1 ];
        
        newArgs[0] = context;
        for ( int i = 0 ; i < args.length ; ++i ) {
            newArgs[i+1] = args[i];
        }
        
        try {
            return this.method.invoke( this.object, newArgs);
        } catch (IllegalAccessException e) {
            throw new DynJSException( e );
        } catch (IllegalArgumentException e) {
            throw new DynJSException( e );
        } catch (InvocationTargetException e) {
            throw new DynJSException( e );
        }
    }

    @Override
    public String[] getArguments() {
        // TODO Auto-generated method stub
        return null;
    }

    private Object object;
    private Method method;
}
