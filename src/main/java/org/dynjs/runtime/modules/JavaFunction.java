package org.dynjs.runtime.modules;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dynjs.api.Function;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.DynThreadContext;

public class JavaFunction implements Function {

	public JavaFunction(Object object, Method method)
			throws IllegalAccessException {
		this.object = object;
		this.method = method;
		this.handle = MethodHandles.lookup().unreflect(method).bindTo(this.object);
		
		int trueNumberOfArgs = -1;
		
		Class<?>[] methodParamTypes = this.method.getParameterTypes();
		if ( methodParamTypes.length >= 2 ) {
			if (methodParamTypes[1].equals(DynThreadContext.class)) {
				trueNumberOfArgs = methodParamTypes.length - 2;
			}
		}
		
		if ( trueNumberOfArgs < 0 ) {
			trueNumberOfArgs = methodParamTypes.length;
		}
		
		this.args = new String[ trueNumberOfArgs ];
		
		for ( int i = 0 ; i < trueNumberOfArgs ; ++i ) {
			this.args[i] = "arg" + i;
		}
	}

	@Override
    public Object call(Object self, DynThreadContext context, Object... arguments) {
        List<Object> newArgs = buildArguments( self, context, arguments);

		try {
			return this.handle.invokeWithArguments(newArgs);
		} catch (Throwable e) {
			throw new DynJSException(e);
		}
	}

	private List<Object> buildArguments(Object self, DynThreadContext context,
			Object... args) {
		List<Object> newArgs = new ArrayList<Object>();

		Class<?>[] methodParamTypes = this.method.getParameterTypes();
		if (methodParamTypes.length >= 2) {
			if (methodParamTypes[1].equals(DynThreadContext.class)) {
				newArgs.add(self);
				newArgs.add(context);
			}
		}

		for (Object arg : args) {
			newArgs.add(arg);
		}

		int additionalNulls = methodParamTypes.length - newArgs.size();
		for (int i = 0; i < additionalNulls; ++i) {
			newArgs.add(null);
		}

		return newArgs;
	}

	@Override
    public String[] getParameters() {
		return this.args;
	}

	private Object object;
	private Method method;
	private MethodHandle handle;
	private String[] args;

}
