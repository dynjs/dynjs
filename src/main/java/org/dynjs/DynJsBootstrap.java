/**
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
 *
 * @author qmx
 */
public class DynJsBootstrap {

    public static CallSite bootstrap(Lookup lookup, String name, MethodType type, Object... args) throws NoSuchMethodException, IllegalAccessException {
        MutableCallSite site = new MutableCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, MutableCallSite.class);
        MethodHandle myFallback = MethodHandles.insertArguments(
                lookup.findStatic(DynJsBootstrap.class, "fallback", fallbackType),
                0,
                site);
        site.setTarget(myFallback);
        return site;
    }

    public static Object fallback(CallSite site, String name, Object... args) throws Throwable {

        MethodHandle target = null;
        if ("wtf".equals(name)) {
            target = MethodHandles.lookup().findStatic(DynJsBootstrap.class, name, site.type());
        }
        Object result = target.invokeWithArguments(args);
        return result;
    }
    public static final MethodHandle FALLBACK;

    static {
        FALLBACK = findStatic(DynJsBootstrap.class, "fallback", MethodType.methodType(CallSite.class, Object[].class));
    }

    public static MethodHandle findStatic(Class caller, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findStatic(caller, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
