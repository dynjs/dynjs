package org.dynjs.runtime.linker.js;

import org.dynjs.runtime.Reference;
import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.filters.SimpleStatelessFilter;

/**
 * @author Bob McWhirter
 */
public class ReferenceStrictnessFilter extends SimpleStatelessFilter {

    public static Filter INSTANCE = new ReferenceStrictnessFilter();

    public static Object filter(Object input) {
        return ((Reference)input).isStrictReference();
    }
}
