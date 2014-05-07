package org.dynjs.runtime.linker.java;

import org.dynjs.runtime.Types;
import org.projectodd.rephract.filters.SimpleStatelessFilter;

/**
 * @author Bob McWhirter
 */
public class NullReplacingFilter extends SimpleStatelessFilter {

    public static NullReplacingFilter INSTANCE = new NullReplacingFilter();

    public static Object filter(Object obj) {
        if ( obj != null ) {
            return obj;
        }

        return Types.UNDEFINED;
    }
}
