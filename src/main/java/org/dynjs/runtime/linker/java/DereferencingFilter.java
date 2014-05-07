package org.dynjs.runtime.linker.java;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.Reference;
import org.projectodd.rephract.filters.Filter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class DereferencingFilter implements Filter {

    public static DereferencingFilter INSTANCE = new DereferencingFilter();

    public static Object filter(Object obj) {
        if ( obj instanceof Reference) {
            return ((Reference)obj).getBase();
        }

        if ( obj instanceof DereferencedReference) {
            return ((DereferencedReference) obj).getReference().getBase();
        }

        return obj;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findStatic(DereferencingFilter.class, "filter", MethodType.methodType(Object.class, Object.class));
    }
}
