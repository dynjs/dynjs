package org.dynjs.runtime.extensions;

import org.dynjs.runtime.linker.anno.CompanionFor;

@CompanionFor(Boolean.class)
public class BooleanOperations {

    public static Boolean lor(Boolean a, Boolean b) {
        return a || b;
    }

}
