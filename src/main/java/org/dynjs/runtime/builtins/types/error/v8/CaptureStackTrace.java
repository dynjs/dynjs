package org.dynjs.runtime.builtins.types.error.v8;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

/**
 * Implements the custom V8 error API as described here https://code.google.com/p/v8/wiki/JavaScriptStackTraceApi
 * @author Lance Ball
 */
public class CaptureStackTrace extends AbstractNativeFunction {
    public CaptureStackTrace(GlobalObject globalObject) {
        super(globalObject, "error", "constructorOpt");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return null;
    }
}
