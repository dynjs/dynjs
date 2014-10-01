package org.dynjs.runtime.modules;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;

public class JavaFunction extends AbstractNativeFunction {

    public JavaFunction(GlobalContext globalContext, Object object, Method method) throws IllegalAccessException {
        super(globalContext);
        this.object = object;
        this.method = method;
        this.handle = MethodHandles.lookup().unreflect(method).bindTo(this.object);

        int trueNumberOfArgs = -1;

        Class<?>[] methodParamTypes = this.method.getParameterTypes();
        if (methodParamTypes.length >= 2) {
            if (methodParamTypes[0].equals(ExecutionContext.class)) {
                trueNumberOfArgs = methodParamTypes.length - 2;
            }
        }

        if (trueNumberOfArgs < 0) {
            trueNumberOfArgs = methodParamTypes.length;
        }

        String[] formalParams = new String[trueNumberOfArgs];

        for (int i = 0; i < trueNumberOfArgs; ++i) {
            formalParams[i] = "arg" + i;
        }

        setFormalParamters(formalParams);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        try {
            List<Object> newArgs = buildArguments(context, self, arguments);
            return this.handle.invokeWithArguments(newArgs.toArray());
        } catch (Throwable e) {
            throw new DynJSException(e);
        }
    }

    private List<Object> buildArguments(ExecutionContext context, Object self, Object... args) {
        List<Object> newArgs = new ArrayList<Object>();

        Class<?>[] methodParamTypes = this.method.getParameterTypes();
        if (methodParamTypes.length > 0) {
            if (methodParamTypes[0].equals(ExecutionContext.class)) {
                newArgs.add(context);
                newArgs.add(self);
            }
            if (methodParamTypes[methodParamTypes.length-1].equals(Object[].class)) {
                newArgs.add(args);
            } else {
                for (Object arg : args) {
                    newArgs.add(arg);
                }
            }
        }

        int additionalNulls = methodParamTypes.length - newArgs.size();
        for (int i = 0; i < additionalNulls; ++i) {
            newArgs.add(null);
        }

        return newArgs;
    }

    public String getFileName() {
        return this.object.getClass().getName().replace(".", "/") + ".java";
    }

    private Object object;
    private Method method;
    private MethodHandle handle;

}
