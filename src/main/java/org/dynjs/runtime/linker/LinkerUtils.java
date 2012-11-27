package org.dynjs.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

import com.headius.invokebinder.Binder;

public class LinkerUtils {

    public static boolean isJavascriptObjectReference(Object object) {
        return (object instanceof Reference) && (((Reference) object).getBase() instanceof JSObject );
    }
    
    public static boolean isJavascriptPrimitiveReference(Object object) {
        if ( ! (object instanceof Reference) ) {
            return false;
        }
        
        Object base = ((Reference)object).getBase();
        
        return base instanceof String || base instanceof Number || base instanceof Boolean;
    }
    
    public static boolean isJavascriptUndefinedReference(Object object) {
        return (object instanceof Reference) && (((Reference) object).getBase() == Types.UNDEFINED );
    }
    
    
    public static boolean isJavascriptFunctionReference(Object object) {
        return (object instanceof Reference) && (((Reference) object).getBase() instanceof JSFunction);
    }

    public static boolean isJavascriptEnvironmnetReference(Object object) {
        return (object instanceof Reference) && (((Reference) object).getBase() instanceof EnvironmentRecord);
    }

    public static boolean isJavaObjectReference(Object object) {
        return (object instanceof Reference) && !(((Reference) object).getBase() == Types.UNDEFINED);
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public static MethodHandle javascriptObjectReferenceGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .invoke(MethodHandles.lookup().findStatic(LinkerUtils.class, "isJavascriptObjectReference", MethodType.methodType(boolean.class, Object.class)));
    }
    
    public static MethodHandle javascriptPrimitiveReferenceGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .invoke(MethodHandles.lookup().findStatic(LinkerUtils.class, "isJavascriptPrimitiveReference", MethodType.methodType(boolean.class, Object.class)));
    }
    
    public static MethodHandle javascriptUndefinedReferenceGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .invoke(MethodHandles.lookup().findStatic(LinkerUtils.class, "isJavascriptUndefinedReference", MethodType.methodType(boolean.class, Object.class)));
    }
    
    public static MethodHandle javascriptFunctionReferenceGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .invoke(MethodHandles.lookup().findStatic(LinkerUtils.class, "isJavascriptFunctionReference", MethodType.methodType(boolean.class, Object.class)));
    }

    public static MethodHandle javascriptEnvironmentReferenceGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        return binder.drop(1, binder.type().parameterCount() - 1)
                .invoke(MethodHandles.lookup().findStatic(LinkerUtils.class, "isJavascriptEnvironmnetReference", MethodType.methodType(boolean.class, Object.class)));
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------


    public static MethodHandle primitiveReferenceBaseFolder() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(LinkerUtils.class, "primitiveReferenceBaseFolder", MethodType.methodType(JSObject.class, ExecutionContext.class, Reference.class ));
    }
    
    public static MethodHandle referenceBaseFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(LinkerUtils.class, "referenceBaseFilter", MethodType.methodType(Object.class, Reference.class));
    }

    public static MethodHandle referenceStrictnessFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(LinkerUtils.class, "referenceStrictnessFilter", MethodType.methodType(boolean.class, Reference.class));
    }
    
    public static MethodHandle globalObjectFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(LinkerUtils.class, "globalObjectFilter", MethodType.methodType(JSObject.class, ExecutionContext.class));
    }
    
    public static JSObject primitiveReferenceBaseFolder(ExecutionContext context, Reference reference) {
        return Types.toObject( context, reference.getBase() );
    }

    public static Object referenceBaseFilter(Reference reference) {
        return reference.getBase();
    }

    public static boolean referenceStrictnessFilter(Reference reference) {
        return reference.isStrictReference();
    }

    public static Object referenceValueFilter(Reference reference, ExecutionContext context) {
        return reference.getValue(context);
    }
    
    public static JSObject globalObjectFilter(ExecutionContext context) {
        return context.getGlobalObject();
    }

}
