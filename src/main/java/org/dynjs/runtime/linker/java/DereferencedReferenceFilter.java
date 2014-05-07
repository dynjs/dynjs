package org.dynjs.runtime.linker.java;

import org.dynjs.codegen.DereferencedReference;
import org.projectodd.rephract.filters.Filter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class DereferencedReferenceFilter implements Filter {

    public static DereferencedReferenceFilter INSTANCE = new DereferencedReferenceFilter();

    public static Object filter(Object obj) {
        if ( obj instanceof DereferencedReference) {
            return ((DereferencedReference) obj).getReference();
        }

        return null;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findStatic(DereferencedReferenceFilter.class, "filter", MethodType.methodType(Object.class, Object.class));
    }
}
