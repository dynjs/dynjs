package org.dynjs.runtime.linker;

import static java.lang.invoke.MethodType.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ThreadContextManager;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.java.JSJavaImplementationManager;
import org.projectodd.rephract.mop.java.CoercionMatrix;

import com.headius.invokebinder.Binder;

public class DynJSCoercionMatrix extends CoercionMatrix {

    private JSJavaImplementationManager manager;

    public DynJSCoercionMatrix(JSJavaImplementationManager manager) throws NoSuchMethodException, IllegalAccessException {
        this.manager = manager;
        Lookup lookup = MethodHandles.lookup();
        addCoercion(3, String.class, JSObject.class, lookup.findStatic(DynJSCoercionMatrix.class, "objectToString", methodType(String.class, JSObject.class)));
        addArrayCoercion(2, DynArray.class, new DynArrayCoercer());
    }

    public static String objectToString(JSObject object) {
        ExecutionContext context = ThreadContextManager.currentContext();
        Object toString = object.get(context, "toString");
        if (toString instanceof JSFunction) {
            return context.call((JSFunction) toString, object).toString();
        }

        return null;
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    @Override
    public int isCompatible(Class<?> target, Object actual) {
        int superCompat = super.isCompatible(target, actual);
        if (superCompat >= 0 && superCompat < 3 ) {
            return superCompat;
        }
        
        Class<?> actualClass = actual.getClass();
        if (actualClass == Types.UNDEFINED.getClass() || actualClass == Types.NULL.getClass()) {
            return 3;
        }

        if (JSFunction.class.isAssignableFrom(actualClass)) {
            if ( isSingleAbstractMethod(target) != null) {
                return 3;
            }
        }
        
        return -1;
    }

    @Override
    public MethodHandle getFilter(Class<?> target, Object actual) {
        int superCompat = super.isCompatible(target, actual);
        
        if (superCompat >= 0 && superCompat < 3 ) {
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
                    return singleAbstractMethod(methodName, target, actualClass);
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

    protected MethodHandle singleAbstractMethod(String methodName, Class<?> target, Class<?> actual) throws NoSuchMethodException, IllegalAccessException {
        Lookup lookup = MethodHandles.lookup();
        MethodHandle method = lookup.findStatic(DynJSCoercionMatrix.class, "singleAbstractMethod", methodType(Object.class, JSJavaImplementationManager.class, Class.class, ExecutionContext.class, String.class, JSObject.class));
        
        return Binder.from(methodType(target, Object.class))
                .insert(0, this.manager)
                .insert(1, target)
                .insert(2, ThreadContextManager.currentContext() )
                .insert(3, methodName )
                .invoke(method);
    }

    public static Object singleAbstractMethod(JSJavaImplementationManager manager, Class<?> targetClass, ExecutionContext context, String methodName, JSObject implementation)
            throws Exception {
        JSObject implObj = new DynObject(context.getGlobalObject());
        implObj.put(context, methodName, implementation, false);
        return manager.getImplementationWrapper(targetClass, context, implObj);
    }
    
    
    public static MethodHandle nullReplacingFilter(Class<?> target) throws NoSuchMethodException, IllegalAccessException {
        return Binder.from( methodType(target, Object.class) )
                .drop(0)
                .insert( 0, new Class[] { Object.class}, new Object[] { null } )
                .invoke( MethodHandles.identity(target));
    }

}
