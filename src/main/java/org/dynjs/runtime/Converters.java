/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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
package org.dynjs.runtime;

import static java.lang.invoke.MethodType.*;

import java.lang.invoke.MethodHandle;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;

public class Converters {

    public static final MethodHandle toBoolean;
    public static final MethodHandle toInteger;

    static {
        toBoolean = Lookup.PUBLIC.findStatic(Converters.class, "toBoolean", methodType(Boolean.class, Object.class));
        toInteger = Lookup.PUBLIC.findStatic(Converters.class, "toInteger", methodType(Integer.class, Double.class));
    }

    public static final GuardedInvocation Guarded_toBoolean = new GuardedInvocation(toBoolean,
            Guards.isInstance(Object.class, methodType(Boolean.class, Object.class)));

    public static Boolean toBoolean(Object value) {
        return Types.toBoolean(value);
    }

    public static Integer toInteger(Double value) {
        return value.intValue();
    }

}
