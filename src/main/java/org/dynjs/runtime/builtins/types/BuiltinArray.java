package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.*;
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

    public BuiltinArray(final GlobalContext globalContext) {
        super(globalContext, "item1");

        final DynArray proto = new DynArray(globalContext);
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setPrototype(globalContext.getPrototypeFor("Object"));

        // Array.prototype.foo()
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalContext));
        defineNonEnumerableProperty(proto, "concat", new Concat(globalContext));
        defineNonEnumerableProperty(proto, "join", new Join(globalContext));
        defineNonEnumerableProperty(proto, "pop", new Pop(globalContext));
        defineNonEnumerableProperty(proto, "push", new Push(globalContext));
        defineNonEnumerableProperty(proto, "reverse", new Reverse(globalContext));
        defineNonEnumerableProperty(proto, "shift", new Shift(globalContext));
        defineNonEnumerableProperty(proto, "slice", new Slice(globalContext));
        defineNonEnumerableProperty(proto, "sort", new Sort(globalContext));
        defineNonEnumerableProperty(proto, "splice", new Splice(globalContext));
        defineNonEnumerableProperty(proto, "unshift", new Unshift(globalContext));
        defineNonEnumerableProperty(proto, "indexOf", new IndexOf(globalContext));
        defineNonEnumerableProperty(proto, "lastIndexOf", new LastIndexOf(globalContext));
        defineNonEnumerableProperty(proto, "every", new Every(globalContext));
        defineNonEnumerableProperty(proto, "some", new Some(globalContext));

        defineNonEnumerableProperty(proto, "forEach", new ForEach(globalContext));
        defineNonEnumerableProperty(proto, "map", new Map(globalContext));
        defineNonEnumerableProperty(proto, "filter", new Filter(globalContext));
        defineNonEnumerableProperty(proto, "reduce", new Reduce(globalContext));
        defineNonEnumerableProperty(proto, "reduceRight", new ReduceRight(globalContext));

        // Array.foo()
        defineNonEnumerableProperty(this, "isArray", new IsArray(globalContext));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {

        DynObject arraySelf = null;

        if (self == Types.UNDEFINED || self == Types.NULL ) {
            arraySelf = new DynArray(context.getGlobalContext());
        } else {
            arraySelf = (DynObject) self;
        }
        if (args.length == 1 && args[0] instanceof Number) {
            arraySelf.defineOwnProperty(context, "length",
                    PropertyDescriptor.newDataPropertyDescriptor(args[0], true, false, false), false);
        } else {
            Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
            int numArgs = (int) argsObj.get(context, "length");
            if (numArgs == 0 ) {
                arraySelf.defineOwnProperty(context, "length",
                        PropertyDescriptor.newDataPropertyDescriptor(0l, true, false, false), false);
            } else {
                arraySelf.defineOwnProperty(context, "length",
                        PropertyDescriptor.newDataPropertyDescriptor((long) args.length, true, false, false), false);
                for (int i = 0; i < args.length; ++i) {
                    final int finalI = i;
                    arraySelf.defineOwnProperty(context, "" + i,
                            PropertyDescriptor.newDataPropertyDescriptor(args[finalI], true, true, true), false);
                }
            }
        }
        return arraySelf;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynArray(context.getGlobalContext());
    }

    // ----------------------------------------------------------------------

    public static DynArray newArray(ExecutionContext context) {
        JSFunction ctor = (JSFunction) context.getGlobalContext().getObject().get(context, "__Builtin_Array");
        return (DynArray) context.construct((Object) null, ctor);
    }

    public static DynArray newArray(ExecutionContext context, long len) {
        JSFunction ctor = (JSFunction) context.getGlobalContext().getObject().get(context, "__Builtin_Array");
        return (DynArray) context.construct((Object) null, ctor, len);
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinArray.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Array>";
    }
}
