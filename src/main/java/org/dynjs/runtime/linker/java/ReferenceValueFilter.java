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
public class ReferenceValueFilter implements Filter {

    public static ReferenceValueFilter INSTANCE = new ReferenceValueFilter();

    public static Object filter(Object obj) {
        if ( obj instanceof Reference) {
            return ((Reference)obj).getBase();
        }

        if ( obj instanceof DereferencedReference) {
            return ((DereferencedReference) obj).getValue();
        }

        return obj;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findStatic(ReferenceValueFilter.class, "filter", MethodType.methodType(Object.class, Object.class));
    }
}
