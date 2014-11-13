package org.dynjs.runtime.linker.js;

import org.dynjs.runtime.ExecutionContext;
import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.filters.SimpleStatelessFilter;

/**
 * @author Bob McWhirter
 */
public class GlobalObjectFilter extends SimpleStatelessFilter {

    public static Filter INSTANCE = new GlobalObjectFilter();

    public static Object filter(Object context) {
        return ((ExecutionContext) context).getGlobalContext().getObject();
    }
}
