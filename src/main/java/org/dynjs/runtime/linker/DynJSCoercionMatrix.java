package org.dynjs.runtime.linker;

import com.headius.invokebinder.Binder;
import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.java.jsimpl.JSJavaImplementationManager;
import org.projectodd.rephract.java.reflect.CoercionMatrix;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;

import static java.lang.invoke.MethodType.methodType;

public class DynJSCoercionMatrix extends CoercionMatrix {

    private JSJavaImplementationManager manager;
    private WeakHashMap<Class<?>, MethodHandle> samCache = new WeakHashMap<>();

    public DynJSCoercionMatrix(JSJavaImplementationManager manager) throws NoSuchMethodException, IllegalAccessException {
        this.manager = manager;
        Lookup lookup = MethodHandles.lookup();

        // Convert JavaScript null and undefined values to Java null
        addCoercion(0, Object.class, Types.Null.class, lookup.findStatic(DynJSCoercionMatrix.class, "jsToJavaNull", methodType(Object.class, Object.class)));

        // Convert JavaScript objects to Strings
        addCoercion(3, String.class, JSObject.class, lookup.findStatic(DynJSCoercionMatrix.class, "objectToString", methodType(String.class, JSObject.class)));

        // Byte conversions
        // TODO: These belong in Rephract's CoercionMatrix
        addCoercion(0, Byte.class, byte.class, MethodHandles.identity(byte.class));
        addCoercion(0, Byte.class, Byte.class, MethodHandles.identity(Byte.class));
        addCoercion(1, Byte.class, Short.class, lookup.findStatic(DynJSCoercionMatrix.class, "numberToByte", methodType(Byte.class, Number.class)));
        addCoercion(1, Byte.class, Integer.class, lookup.findStatic(DynJSCoercionMatrix.class, "numberToByte", methodType(Byte.class, Number.class)));
        addCoercion(1, Byte.class, Long.class, lookup.findStatic(DynJSCoercionMatrix.class, "numberToByte", methodType(Byte.class, Number.class)));
        addCoercion(2, Byte.class, Double.class, lookup.findStatic(DynJSCoercionMatrix.class, "numberToByte", methodType(Byte.class, Number.class)));
        addCoercion(2, Byte.class, Float.class, lookup.findStatic(DynJSCoercionMatrix.class, "numberToByte", methodType(Byte.class, Number.class)));

        DynArrayCoercer dynArrayCoercer = new DynArrayCoercer();
        addArrayCoercion(1, boolean[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, byte[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, char[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, double[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, float[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, int[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, long[].class, DynArray.class, dynArrayCoercer);
        addArrayCoercion(1, short[].class, DynArray.class, dynArrayCoercer);
        // Object[] will catch all non-primitive types
        addArrayCoercion(2, Object[].class, DynArray.class, dynArrayCoercer);
    }

    public static String objectToString(JSObject object) {
        ExecutionContext context = ThreadContextManager.currentContext();
        Object toString = object.get(context, "toString");
        if (toString instanceof JSFunction) {
            return context.call((JSFunction) toString, object).toString();
        }

        return null;
    }

    public static Object jsToJavaNull(Object jsNull) {
        return null;
    }

    public static Byte numberToByte(Number value) {
        return value.byteValue();
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    @Override
    public int isCompatible(Class<?> target, Object actual) {
        int superCompat = super.isCompatible(target, actual);
        if (superCompat >= 0 && superCompat < 3) {
            return superCompat;
        }

        Class<?> actualClass = actual.getClass();
        if (actualClass == Types.UNDEFINED.getClass() || actualClass == Types.NULL.getClass()) {
            return 3;
        }

        if (JSFunction.class.isAssignableFrom(actualClass)) {
            if (isSingleAbstractMethod(target) != null) {
                return 3;
            }
        }

        return -1;
    }

    public static boolean coerceToBoolean(Object value) {
        return (boolean) DynJSBootstrapper.COERCION_MATRIX.coerceTo( value, boolean.class );
    }

    public Object coerceTo(Object value, Class<?> toType) {
        MethodHandle filter = getFilter(toType, value);
        if ( filter == null ) {
            return null;
        }

        try {
            return filter.invoke(value);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public MethodHandle getFilter(Class<?> target, Object actual) {
        int superCompat = super.isCompatible(target, actual);

        if (superCompat >= 0 && superCompat < 3) {
            return super.getFilter(target, actual);
        }

        Class<?> actualClass = actual.getClass();
        if (actualClass == Types.UNDEFINED.getClass() || actualClass == Types.NULL.getClass()) {
            try {
                return nullReplacingFilter(target);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (JSFunction.class.isAssignableFrom(actualClass)) {
            try {
                String methodName = isSingleAbstractMethod(target);
                if (methodName != null) {
                    return singleAbstractMethod(methodName, target);
                }
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return MethodHandles.identity(actualClass);
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    protected String isSingleAbstractMethod(Class<?> target) {
        String methodName = null;

        Method[] methods = target.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isAbstract(modifiers)) {
                if (methodName == null) {
                    methodName = methods[i].getName();
                } else if (!methodName.equals(methods[i].getName())) {
                    return null;
                }
            }
        }

        return methodName;
    }

    protected synchronized MethodHandle singleAbstractMethod(String methodName, Class<?> target) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle cached = this.samCache.get(target);
        if (cached == null) {
            Lookup lookup = MethodHandles.lookup();
            MethodHandle method = lookup.findStatic(DynJSCoercionMatrix.class, "singleAbstractMethod", methodType(Object.class, JSJavaImplementationManager.class, Class.class, ExecutionContext.class, String.class, JSObject.class));

            cached = Binder.from(methodType(target, Object.class))
                    .insert(0, this.manager)
                    .insert(1, target)
                    .insert(2, ThreadContextManager.currentContext())
                    .insert(3, methodName)
                    .invoke(method);
            this.samCache.put( target, cached );
        }
        return cached;
    }

    public static Object singleAbstractMethod(JSJavaImplementationManager manager, Class<?> targetClass, ExecutionContext context, String methodName, JSObject implementation)
            throws Exception {
        JSObject implObj = new DynObject(context.getGlobalContext());
        implObj.put(context, methodName, implementation, false);
        return manager.getImplementationWrapper(targetClass, context, implObj);
    }


    public static MethodHandle nullReplacingFilter(Class<?> target) throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        MethodHandle returnNull = lookup.findStatic(DynJSCoercionMatrix.class, "returnNull", methodType(Object.class, Object.class));
        return Binder.from(methodType(target, Object.class))
                //.drop(0)
                //.insert(0, new Class[]{Object.class}, new Object[]{null})
                .invoke(returnNull);
    }

    public static Object returnNull(Object arg) {
        return null;
    }

}
