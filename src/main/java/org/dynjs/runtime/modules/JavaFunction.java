package org.dynjs.runtime.modules;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;

public class JavaFunction extends AbstractNativeFunction {

    public JavaFunction(Object object, Method method) throws IllegalAccessException {
        super( null, false );
        this.object = object;
        this.method = method;
        this.handle = MethodHandles.lookup().unreflect( method ).bindTo( this.object );

        int trueNumberOfArgs = -1;

        Class<?>[] methodParamTypes = this.method.getParameterTypes();
        if (methodParamTypes.length >= 1) {
            if (methodParamTypes[0].equals( ExecutionContext.class )) {
                trueNumberOfArgs = methodParamTypes.length - 1;
            }
        }

        if (trueNumberOfArgs < 0) {
            trueNumberOfArgs = methodParamTypes.length;
        }

        String[] formalParams = new String[trueNumberOfArgs];

        for (int i = 0; i < trueNumberOfArgs; ++i) {
            formalParams[i] = "arg" + i;
        }
        
        setFormalParamters( formalParams );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        List<Object> newArgs = buildArguments( context, self, arguments );

        try {
            return this.handle.invokeWithArguments( newArgs );
        } catch (Throwable e) {
            throw new DynJSException( e );
        }
    }

    private List<Object> buildArguments(ExecutionContext context, Object self, Object... args) {
        List<Object> newArgs = new ArrayList<Object>();

        Class<?>[] methodParamTypes = this.method.getParameterTypes();
        if (methodParamTypes.length >= 2) {
            if (methodParamTypes[1].equals( ExecutionContext.class )) {
                newArgs.add( self );
                newArgs.add( context );
            }
        }

        for (Object arg : args) {
            newArgs.add( arg );
        }

        int additionalNulls = methodParamTypes.length - newArgs.size();
        for (int i = 0; i < additionalNulls; ++i) {
            newArgs.add( null );
        }

        return newArgs;
    }

    private Object object;
    private Method method;
    private MethodHandle handle;

}
