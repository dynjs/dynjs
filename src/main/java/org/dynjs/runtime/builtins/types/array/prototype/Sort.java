package org.dynjs.runtime.builtins.types.array.prototype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Sort extends AbstractNativeFunction {

    public Sort(GlobalObject globalObject) {
        super(globalObject, "compareFn");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.11
        JSObject o = Types.toObject(context, self);
        int len = Types.toInteger(context, o.get(context, "length"));

        List<Integer> indices = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (int i = 0; i < len; ++i) {
            indices.add(i);
            values.add(o.get(context, "" + i));
        }

        Object compareFn = args[0];

        if (compareFn == Types.UNDEFINED) {
            Collections.sort(indices, new SortComparator(context, o));
        } else {
            Collections.sort(indices, new SortComparator(context, o, (JSFunction) compareFn));
        }

        for ( int i = 0 ; i < len ; ++i ) {
            Integer index = indices.get( i );
            Object value = values.get( index );
            
            if ( value == Types.UNDEFINED ) {
                o.delete(context, ""+i, false);
            } else {
                o.put(context, ""+i, value, false);
            }
        }
        
        return o;

    }
}
