package org.dynjs.runtime.linker.js;

import org.dynjs.runtime.Reference;
import org.projectodd.rephract.filters.Filter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class ReferenceStrictnessFilter implements Filter {

    public static Filter INSTANCE = new ReferenceStrictnessFilter();

    public static boolean filter(Object input) {
        return ((Reference)input).isStrictReference();
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findStatic( ReferenceStrictnessFilter.class, "filter", MethodType.methodType( boolean.class, Object.class ) );
    }
}
