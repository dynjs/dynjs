package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
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
        super(globalObject, "item1");

        final DynArray proto = new DynArray(globalObject);
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));

        // Array.prototype.foo()
        proto.forceDefineNonEnumerableProperty( "constructor", this);
        proto.forceDefineNonEnumerableProperty( "toString", new ToString(globalObject));
        proto.forceDefineNonEnumerableProperty("toLocaleString", new ToLocaleString(globalObject));
        proto.forceDefineNonEnumerableProperty("concat", new Concat(globalObject));
        proto.forceDefineNonEnumerableProperty("join", new Join(globalObject));
        proto.forceDefineNonEnumerableProperty("pop", new Pop(globalObject));
        proto.forceDefineNonEnumerableProperty("push", new Push(globalObject));
        proto.forceDefineNonEnumerableProperty("reverse", new Reverse(globalObject));
        proto.forceDefineNonEnumerableProperty("shift", new Shift(globalObject));
        proto.forceDefineNonEnumerableProperty("slice", new Slice(globalObject));
        proto.forceDefineNonEnumerableProperty("sort", new Sort(globalObject));
        proto.forceDefineNonEnumerableProperty("splice", new Splice(globalObject));
        proto.forceDefineNonEnumerableProperty("unshift", new Unshift(globalObject));
        proto.forceDefineNonEnumerableProperty("indexOf", new IndexOf(globalObject));
        proto.forceDefineNonEnumerableProperty( "lastIndexOf", new LastIndexOf(globalObject));
        proto.forceDefineNonEnumerableProperty( "every", new Every(globalObject));
        proto.forceDefineNonEnumerableProperty( "some", new Some(globalObject));

        proto.forceDefineNonEnumerableProperty("forEach", new ForEach(globalObject));
        proto.forceDefineNonEnumerableProperty("map", new Map(globalObject));
        proto.forceDefineNonEnumerableProperty("filter", new Filter(globalObject));
        proto.forceDefineNonEnumerableProperty("reduce", new Reduce(globalObject));
        proto.forceDefineNonEnumerableProperty("reduceRight", new ReduceRight(globalObject));

        // Array.foo()
        this.forceDefineNonEnumerableProperty("isArray", new IsArray(globalObject));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {

        DynArray arraySelf = null;

        if (self == Types.UNDEFINED || self == Types.NULL) {
            arraySelf = new DynArray(context.getGlobalObject());
        } else {
            arraySelf = (DynArray) self;
        }
        if (args.length == 1 && args[0] instanceof Number) {
            arraySelf.defineOwnProperty(context, "length", new PropertyDescriptor() {
                {
                    set("Value", args[0]);
                    set("Writable", true);
                    set("Enumerable", false);
                    set("Configurable", false);
                }
            }, false);
        } else {
            Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
            int numArgs = (int) argsObj.get(context, "length");
            if (numArgs == 0) {
                arraySelf.defineOwnProperty(context, "length", new PropertyDescriptor() {
                    {
                        set("Value", 0L);
                        set("Writable", true);
                        set("Enumerable", false);
                        set("Configurable", false);
                    }
                }, false);
            } else {
                arraySelf.defineOwnProperty(context, "length", new PropertyDescriptor() {
                    {
                        set("Value", (long) args.length);
                        set("Writable", true);
                        set("Enumerable", false);
                        set("Configurable", false);
                    }
                }, false);
                for (int i = 0; i < args.length; ++i) {
                    final int finalI = i;
                    arraySelf.defineOwnProperty(context, "" + i, new PropertyDescriptor() {
                        {
                            set("Value", args[finalI]);
                            set("Writable", true);
                            set("Enumerable", true);
                            set("Configurable", true);
                        }
                    }, false);
                }
            }
        }
        return arraySelf;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynArray(context.getGlobalObject());
    }

    // ----------------------------------------------------------------------

    public static DynArray newArray(ExecutionContext context) {
        JSFunction ctor = (JSFunction) context.getGlobalObject().get(context, "__Builtin_Array");
        return (DynArray) context.construct(ctor);
    }

    public static DynArray newArray(ExecutionContext context, long len) {
        JSFunction ctor = (JSFunction) context.getGlobalObject().get(context, "__Builtin_Array");
        return (DynArray) context.construct(ctor, len);
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
