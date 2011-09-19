/*
   Copyright 2009-2011 Attila Szegedi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.dynalang.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods for creating typical guards. TODO: introduce reasonable
 * caching of created guards.
 *
 * @author Attila Szegedi
 * @version $Id: $
 */
public class Guards {
    private static final Logger LOG = Logger.getLogger(Guards.class.getName(), "org.dynalang.dynalink.support.messages");
    /**
     * Creates a guard method handle with arguments of a specified type, but
     * with boolean return value. When invoked, it returns true if the first
     * argument is of the specified class (exactly of it, not a subclass). The
     * rest of the arguments will be ignored.
     *
     * @param clazz the class of the first argument to test for
     * @param type the method type
     * @return a method handle testing whether its first argument is of the
     * specified class.
     */
    public static MethodHandle isOfClass(Class<?> clazz, MethodType type) {
        final Class<?> declaredType = type.parameterType(0);
        if(clazz == declaredType) {
            LOG.log(Level.WARNING, "isOfClassGuardAlwaysTrue",
                    new Object[] { clazz.getName(), 0, type });
            return constantTrue(type);
        }
        if(!declaredType.isAssignableFrom(clazz)) {
            LOG.log(Level.WARNING, "isOfClassGuardAlwaysFalse",
                    new Object[] { clazz.getName(), 0, type });
            return constantFalse(type);
        }
        return getClassBoundArgumentTest(IS_OF_CLASS, clazz, 0, type);
    }

    /**
     * Creates a method handle with arguments of a specified type, but with
     * boolean return value. When invoked, it returns true if the first argument
     * is instance of the specified class or its subclass). The rest of the
     * arguments will be ignored.
     *
     * @param clazz the class of the first argument to test for
     * @param type the method type
     * @return a method handle testing whether its first argument is of the
     * specified class or subclass.
     */
    public static MethodHandle isInstance(Class<?> clazz, MethodType type) {
        return isInstance(clazz, 0, type);
    }

    /**
     * Creates a method handle with arguments of a specified type, but with
     * boolean return value. When invoked, it returns true if the n'th argument
     * is instance of the specified class or its subclass). The rest of the
     * arguments will be ignored.
     *
     * @param clazz the class of the first argument to test for
     * @param pos the position on the argument list to test
     * @param type the method type
     * @return a method handle testing whether its first argument is of the
     * specified class or subclass.
     */
    public static MethodHandle isInstance(Class<?> clazz, int pos,
            MethodType type) {
        final Class<?> declaredType = type.parameterType(pos);
        if(clazz.isAssignableFrom(declaredType)) {
            LOG.log(Level.WARNING, "isInstanceGuardAlwaysTrue",
                    new Object[] { clazz.getName(), pos, type });
            return constantTrue(type);
        }
        if(!declaredType.isAssignableFrom(clazz)) {
            LOG.log(Level.WARNING, "isInstanceGuardAlwaysFalse",
                    new Object[] { clazz.getName(), pos, type });
            return constantFalse(type);
        }
        return getClassBoundArgumentTest(IS_INSTANCE, clazz, pos, type);
    }

    /**
     * Creates a method handle that returns true if the argument in the
     * specified position is a Java array.
     *
     * @param pos the position in the argument lit
     * @param type the method type of the handle
     * @return a method handle that returns true if the argument in the
     * specified position is a Java array; the rest of the arguments are
     * ignored.
     */
    public static MethodHandle isArray(int pos, MethodType type) {
        final Class<?> declaredType = type.parameterType(pos);
        if(declaredType.isArray()) {
            LOG.log(Level.WARNING, "isArrayGuardAlwaysTrue", new Object[] { pos, type });
            return constantTrue(type);
        }
        if(!declaredType.isAssignableFrom(Object[].class)) {
            LOG.log(Level.WARNING, "isArrayGuardAlwaysFalse", new Object[] { pos, type });
            return constantFalse(type);
        }
        return getArgumentTest(IS_ARRAY, pos, type);
    }

    /**
     * Return true if it is safe to strongly reference a class from the referred
     * class loader from a class associated with the referring class loader
     * without risking a class loader memory leak.
     *
     * @param referrerLoader the referrer class loader
     * @param referredLoader the referred class loader
     * @return true if it is safe to strongly reference the class
     */
    public static boolean canReferenceDirectly(ClassLoader referrerLoader,
            final ClassLoader referredLoader) {
        if(referredLoader == null) {
            // Can always refer directly to a system class
            return true;
        }
        if(referrerLoader == null) {
            // System classes can't refer directly to any non-system class
            return false;
        }
        // Otherwise, can only refer directly to classes residing in same or
        // parent class loader.
        do {
            if(referrerLoader == referredLoader) {
                return true;
            }
            referrerLoader = referrerLoader.getParent();
        } while(referrerLoader != null);
        return false;
    }

    private static MethodHandle getClassBoundArgumentTest(MethodHandle test, Class<?> clazz,
            int pos, MethodType type) {
        // Bind the class to the first argument of the test
        return getArgumentTest(MethodHandles.insertArguments(test, 0, clazz), pos, type);
    }

    private static MethodHandle getArgumentTest(MethodHandle test, int pos, MethodType type) {
        if(type.parameterCount() < 1) {
            throw new IllegalArgumentException(
                    "method type must specify at least one argument");
        }

        return MethodHandles.permuteArguments(test.asType(test.type().changeParameterType(0,
                type.parameterType(pos))), type.changeReturnType(Boolean.TYPE), new int[] { pos });
    }

    private static final MethodHandle IS_OF_CLASS =
            Lookup.PUBLIC.findStatic(Guards.class, "_isOfClass", MethodType
                    .methodType(Boolean.TYPE, Class.class, Object.class));

    private static final MethodHandle IS_INSTANCE =
            Lookup.PUBLIC.findVirtual(Class.class, "isInstance", MethodType
                    .methodType(Boolean.TYPE, Object.class));

    private static final MethodHandle IS_ARRAY =
            Lookup.PUBLIC.findStatic(Guards.class, "_isArray", MethodType
                    .methodType(Boolean.TYPE, Object.class));

    /**
     * This method is public for implementation reasons. Do not invoke it
     * directly. Determines whether the passed object is a Java array.
     *
     * @param o an object
     * @return true if o is an instance of a Java array.
     */
    public static boolean _isArray(Object o) {
        return o != null && o.getClass().isArray();
    }

    /**
     * This method public for implementation reasons. Do not invoke directly.
     * Determines whether the class of the object is what's expected to be.
     *
     * @param c the class
     * @param o the object
     * @return true if the object is exactly of the class, false otherwise.
     */
    public static boolean _isOfClass(Class<?> c, Object o) {
        return o != null && o.getClass() == c;
    }

    private static MethodHandle constantTrue(MethodType type) {
        return constantBoolean(Boolean.TRUE, type);
    }

    private static MethodHandle constantFalse(MethodType type) {
        return constantBoolean(Boolean.FALSE, type);
    }

    private static MethodHandle constantBoolean(Boolean value, MethodType type) {
        return MethodHandles.permuteArguments(MethodHandles.constant(Boolean.TYPE, value),
                type.changeReturnType(Boolean.TYPE));
    }
}