package org.dynjs.runtime.builtins.types.array.prototype;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class Concat extends AbstractNonConstructorFunction {

    public Concat(GlobalContext globalContext) {
        super(globalContext, "item1");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.4
        JSObject o = Types.toObject(context, self);

        DynArray array = new DynArray(context.getGlobalContext());

        List<Object> items = new ArrayList<>();
        items.add(o);

        if (args[0] != Types.UNDEFINED) {
            for (int i = 0; i < args.length; ++i) {
                items.add(args[i]);
            }
        }

        int n = 0;
        for (Object e : items) {
            if (e instanceof JSObject && ((JSObject) e).getClassName().equals("Array")) {
                JSObject jsE = (JSObject) e;

                long len = Types.toInteger(context, jsE.get(context, "length"));

                for (long k = 0; k < len; ++k) {
                    final Object subElement = jsE.get(context, "" + k);
                    array.defineOwnProperty(context, "" + n,
                            PropertyDescriptor.newDataPropertyDescriptor(subElement, true, true, true), false);
                    ++n;
                }
            } else {
                final Object finalE = e;
                array.defineOwnProperty(context, "" + n,
                        PropertyDescriptor.newDataPropertyDescriptor(finalE, true, true, true), false);
                ++n;
            }
        }

        return array;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Concat.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: concat>";
    }
}
