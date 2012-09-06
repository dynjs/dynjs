package org.dynjs.runtime.builtins.types.array.prototype;

import java.util.Comparator;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class SortComparator implements Comparator<Long> {

    private ExecutionContext context;
    private JSObject o;
    private JSFunction compareFn;

    public SortComparator(ExecutionContext context, JSObject o) {
        this(context, o, null);
    }

    public SortComparator(ExecutionContext context, JSObject o, JSFunction compareFn) {
        this.context = context;
        this.o = o;
        this.compareFn = compareFn;
    }

    @Override
    public int compare(Long j, Long k) {
        boolean hasJ = o.hasProperty(context, "" + j);
        boolean hasK = o.hasProperty(context, "" + k);

        if (!hasJ && !hasK) {
            return 0;
        }

        if (!hasJ) {
            return 1;
        }

        if (!hasK) {
            return -1;
        }

        Object x = o.get(context, "" + j);
        Object y = o.get(context, "" + k);

        if (x == Types.UNDEFINED && y == Types.UNDEFINED) {
            return 0;
        }

        if (x == Types.UNDEFINED) {
            return 1;
        }

        if (y == Types.UNDEFINED) {
            return -1;
        }

        if (compareFn != null) {
            return (int)(long) context.call(compareFn, Types.UNDEFINED, x, y);
        }

        String xStr = Types.toString(context, x);
        String yStr = Types.toString(context, y);

        return xStr.compareTo(yStr);
    }

}
