package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.array.IsArray;
import org.dynjs.runtime.builtins.types.array.prototype.Concat;
import org.dynjs.runtime.builtins.types.array.prototype.Every;
import org.dynjs.runtime.builtins.types.array.prototype.Filter;
import org.dynjs.runtime.builtins.types.array.prototype.ForEach;
import org.dynjs.runtime.builtins.types.array.prototype.IndexOf;
import org.dynjs.runtime.builtins.types.array.prototype.Join;
import org.dynjs.runtime.builtins.types.array.prototype.LastIndexOf;
import org.dynjs.runtime.builtins.types.array.prototype.Map;
import org.dynjs.runtime.builtins.types.array.prototype.Pop;
import org.dynjs.runtime.builtins.types.array.prototype.Push;
import org.dynjs.runtime.builtins.types.array.prototype.Reduce;
import org.dynjs.runtime.builtins.types.array.prototype.ReduceRight;
import org.dynjs.runtime.builtins.types.array.prototype.Reverse;
import org.dynjs.runtime.builtins.types.array.prototype.Shift;
import org.dynjs.runtime.builtins.types.array.prototype.Slice;
import org.dynjs.runtime.builtins.types.array.prototype.Some;
import org.dynjs.runtime.builtins.types.array.prototype.Sort;
import org.dynjs.runtime.builtins.types.array.prototype.Splice;
import org.dynjs.runtime.builtins.types.array.prototype.ToLocaleString;
import org.dynjs.runtime.builtins.types.array.prototype.ToString;
import org.dynjs.runtime.builtins.types.array.prototype.Unshift;

public class BuiltinArray extends AbstractBuiltinType {

    public BuiltinArray(final GlobalObject globalObject) {
        super(globalObject);

        final DynArray proto = new DynArray(globalObject);
        put(null, "prototype", proto, false);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        proto.put(null, "constructor", this, false);
        proto.put(null, "toString", new ToString(globalObject), false);
        proto.put(null, "toLocaleString", new ToLocaleString(globalObject), false);
        proto.put(null, "concat", new Concat(globalObject), false);
        proto.put(null, "join", new Join(globalObject), false);
        proto.put(null, "pop", new Pop(globalObject), false);
        proto.put(null, "push", new Push(globalObject), false);
        proto.put(null, "reverse", new Reverse(globalObject), false);
        proto.put(null, "shift", new Shift(globalObject), false);
        proto.put(null, "slice", new Slice(globalObject), false);
        proto.put(null, "sort", new Sort(globalObject), false);
        proto.put(null, "splice", new Splice(globalObject), false);
        proto.put(null, "unshift", new Unshift(globalObject), false);
        proto.put(null, "indexOf", new IndexOf(globalObject), false);
        proto.put(null, "lastIndexOf", new LastIndexOf(globalObject), false);
        proto.put(null, "every", new Every(globalObject), false);
        proto.put(null, "some", new Some(globalObject), false);

        proto.put(null, "forEach", new ForEach(globalObject), false);
        proto.put(null, "map", new Map(globalObject), false);
        proto.put(null, "filter", new Filter(globalObject), false);
        proto.put(null, "reduce", new Reduce(globalObject), false);
        proto.put(null, "reduceRight", new ReduceRight(globalObject), false);

        put(null, "isArray", new IsArray(globalObject), false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        DynArray arraySelf = (DynArray) self;

        if (self != Types.UNDEFINED) {
            if (args.length == 1) {
                final Number possiblyLen = Types.toNumber(context, args[0]);
                if ((possiblyLen instanceof Double) && ((Double) possiblyLen).isNaN()) {
                    arraySelf.setLength(1);
                    arraySelf.setElement(0, args[0]);
                } else {
                    arraySelf.setLength(possiblyLen.intValue());
                }
            } else {
                arraySelf.setLength(args.length);
                for (int i = 0; i < args.length; ++i) {
                    arraySelf.setElement(i, args[i]);
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynArray(context.getGlobalObject());
    }

    // ----------------------------------------------------------------------

    public static DynArray newArray(ExecutionContext context) {
        BuiltinArray ctor = (BuiltinArray) context.getGlobalObject().get(context, "Array");
        return (DynArray) context.construct(ctor);
    }
}
