package org.dynjs.runtime.linker;

import org.dynjs.runtime.DynArray;
import org.projectodd.rephract.mop.java.ArrayCoercer;

public class DynArrayCoercer extends ArrayCoercer {
    @Override
    public Object[] convertToObjectArray(Object value) {
        DynArray dynArray = (DynArray) value;
        int length = (int) dynArray.length();
        Object[] converted = new Object[length];
        for (int i = 0; i < length; i++) {
            converted[i] = dynArray.get(i);
        }
        return converted;
    }
}
