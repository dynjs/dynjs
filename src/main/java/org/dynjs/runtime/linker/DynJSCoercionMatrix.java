package org.dynjs.runtime.linker;

import static java.lang.invoke.MethodType.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ThreadContextManager;
import org.projectodd.linkfusion.mop.java.CoercionMatrix;

public class DynJSCoercionMatrix extends CoercionMatrix {

    public DynJSCoercionMatrix() throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        addCoercion( String.class, JSObject.class, lookup.findStatic(DynJSCoercionMatrix.class, "objectToString", methodType( String.class, JSObject.class ) ) );
    }

    public static String objectToString(JSObject object) {
        ExecutionContext context = ThreadContextManager.currentContext();
        Object toString = object.get(context, "toString");
        if (toString instanceof JSFunction) {
            return context.call((JSFunction) toString, object).toString();
        }

        return null;
    }

}
