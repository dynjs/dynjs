package org.dynjs.runtime.linker.js;

import org.dynjs.runtime.Reference;
import org.projectodd.rephract.filters.Filter;
import org.projectodd.rephract.filters.SimpleStatelessFilter;

/**
 * @author Bob McWhirter
 */
public class ReferenceBaseFilter extends SimpleStatelessFilter {

    public static Filter INSTANCE = new ReferenceBaseFilter();

    public static Object filter(Object input) {
        return ((Reference)input).getBase();
    }
}
