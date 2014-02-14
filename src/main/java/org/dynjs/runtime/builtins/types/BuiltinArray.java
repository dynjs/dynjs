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
import org.dynjs.runtime.PropertyDescriptor.Names;

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
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalObject));
        defineNonEnumerableProperty(proto, "concat", new Concat(globalObject));
        defineNonEnumerableProperty(proto, "join", new Join(globalObject));
        defineNonEnumerableProperty(proto, "pop", new Pop(globalObject));
        defineNonEnumerableProperty(proto, "push", new Push(globalObject));
        defineNonEnumerableProperty(proto, "reverse", new Reverse(globalObject));
        defineNonEnumerableProperty(proto, "shift", new Shift(globalObject));
        defineNonEnumerableProperty(proto, "slice", new Slice(globalObject));
        defineNonEnumerableProperty(proto, "sort", new Sort(globalObject));
        defineNonEnumerableProperty(proto, "splice", new Splice(globalObject));
        defineNonEnumerableProperty(proto, "unshift", new Unshift(globalObject));
        defineNonEnumerableProperty(proto, "indexOf", new IndexOf(globalObject));
        defineNonEnumerableProperty(proto, "lastIndexOf", new LastIndexOf(globalObject));
        defineNonEnumerableProperty(proto, "every", new Every(globalObject));
        defineNonEnumerableProperty(proto, "some", new Some(globalObject));

        defineNonEnumerableProperty(proto, "forEach", new ForEach(globalObject));
        defineNonEnumerableProperty(proto, "map", new Map(globalObject));
        defineNonEnumerableProperty(proto, "filter", new Filter(globalObject));
        defineNonEnumerableProperty(proto, "reduce", new Reduce(globalObject));
        defineNonEnumerableProperty(proto, "reduceRight", new ReduceRight(globalObject));

        defineNonEnumerableProperty(proto, "__proto__", proto);

        // Array.foo()
        defineNonEnumerableProperty(this, "isArray", new IsArray(globalObject));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {

        DynArray arraySelf = null;

        if (self == Types.UNDEFINED || self == Types.NULL ) {
            arraySelf = new DynArray(context.getGlobalObject());
        } else {
            arraySelf = (DynArray) self;
        }
        if (args.length == 1 && args[0] instanceof Number) {
            PropertyDescriptor lengthDesc = new PropertyDescriptor();
            lengthDesc.set(Names.VALUE, args[0]);
            lengthDesc.set(Names.WRITABLE, true);
            lengthDesc.set(Names.ENUMERABLE, false);
            lengthDesc.set(Names.CONFIGURABLE, false);
            arraySelf.defineOwnProperty(context, "length", lengthDesc, false);
        } else {
            Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
            int numArgs = (int) argsObj.get(context, "length");
            if (numArgs == 0 ) {
                PropertyDescriptor lengthDesc = new PropertyDescriptor();
                lengthDesc.set(Names.VALUE, 0L);
                lengthDesc.set(Names.WRITABLE, true);
                lengthDesc.set(Names.ENUMERABLE, false);
                lengthDesc.set(Names.CONFIGURABLE, false);
                arraySelf.defineOwnProperty(context, "length", lengthDesc, false);
            } else {
                PropertyDescriptor lengthDesc = new PropertyDescriptor();
                lengthDesc.set(Names.VALUE, (long) args.length);
                lengthDesc.set(Names.WRITABLE, true);
                lengthDesc.set(Names.ENUMERABLE, false);
                lengthDesc.set(Names.CONFIGURABLE, false);
                arraySelf.defineOwnProperty(context, "length", lengthDesc, false);
                for (int i = 0; i < args.length; ++i) {
                    final int finalI = i;
                    PropertyDescriptor desc = new PropertyDescriptor();
                    desc.set( Names.VALUE, args[finalI] );
                    desc.set( Names.WRITABLE, true );
                    desc.set( Names.ENUMERABLE, true );
                    desc.set( Names.CONFIGURABLE, true );
                    arraySelf.defineOwnProperty(context, "" + i, desc, false);
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
