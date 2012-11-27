package org.dynjs.runtime.linker;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ReferenceContext;
import org.dynjs.runtime.Types;

public class Filters {

    public static ExecutionContext filterContext(ReferenceContext context) {
        return context.getContext();
    }

    public static boolean filterReferenceStrictness(ReferenceContext context) {
        return context.getReference().isStrictReference();
    }

    public static Object filterReturn(Object val) {
        if (val == null) {
            return Types.UNDEFINED;
        }
        return val;
    }

    public static Object filterToJava(ExecutionContext context, Object obj) {
        if (obj instanceof JSObject) {
            JSObject jsObj = (JSObject) obj;
            Object toJava = jsObj.get(context, "toJava");
            if (toJava instanceof JSFunction) {
                Object javaObj = context.internalCall(null, (JSFunction) toJava, obj);
                System.err.println( "got: " + javaObj );
                System.err.println( "converted " + obj + " to " + javaObj + " using " + toJava  );
                return javaObj;
            }
        }
        return obj;
    }

}
