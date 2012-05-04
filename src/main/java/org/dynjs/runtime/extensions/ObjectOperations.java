package org.dynjs.runtime.extensions;

import org.dynjs.runtime.linker.anno.CompanionFor;

@CompanionFor(Object.class)
public class ObjectOperations {

    public static Boolean eq(Object o1, Object o2) {
        return o1.equals(o2);
    }
}
