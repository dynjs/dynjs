package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.string.DynString;
import org.dynjs.runtime.builtins.types.string.FromCharCode;
import org.dynjs.runtime.builtins.types.string.prototype.CharAt;
import org.dynjs.runtime.builtins.types.string.prototype.CharCodeAt;
import org.dynjs.runtime.builtins.types.string.prototype.Concat;
import org.dynjs.runtime.builtins.types.string.prototype.IndexOf;
import org.dynjs.runtime.builtins.types.string.prototype.LastIndexOf;
import org.dynjs.runtime.builtins.types.string.prototype.LocaleCompare;
import org.dynjs.runtime.builtins.types.string.prototype.Match;
import org.dynjs.runtime.builtins.types.string.prototype.Search;
import org.dynjs.runtime.builtins.types.string.prototype.Slice;
import org.dynjs.runtime.builtins.types.string.prototype.Split;
import org.dynjs.runtime.builtins.types.string.prototype.Substring;
import org.dynjs.runtime.builtins.types.string.prototype.ToLowerCase;
import org.dynjs.runtime.builtins.types.string.prototype.ToString;
import org.dynjs.runtime.builtins.types.string.prototype.ToUpperCase;
import org.dynjs.runtime.builtins.types.string.prototype.ValueOf;

public class BuiltinString extends AbstractBuiltinType {

    public BuiltinString(final GlobalObject globalObject) {
        super(globalObject, "value");

        final DynObject proto = new DynObject(globalObject);
        proto.setClassName("String");
        put(null, "prototype", proto, false);
    }

    public void initialize(GlobalObject globalObject, JSObject proto) {
        put(null, "fromCharCode", new FromCharCode(globalObject), false);
        
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        proto.put(null, "constructor", this, false);
        proto.put(null, "toString", new ToString(globalObject), false);
        proto.put(null, "valueOf", new ValueOf(globalObject), false);
        proto.put(null, "charAt", new CharAt(globalObject), false);
        proto.put(null, "charCodeAt", new CharCodeAt(globalObject), false);
        proto.put(null, "concat", new Concat(globalObject), false);
        proto.put(null, "indexOf", new IndexOf(globalObject), false);
        proto.put(null, "lastIndexOf", new LastIndexOf(globalObject), false);
        proto.put(null, "localeCompare", new LocaleCompare(globalObject), false);
        proto.put(null, "match", new Match(globalObject), false);
        proto.put(null, "search", new Search(globalObject), false);
        proto.put(null, "slice", new Slice(globalObject), false);
        proto.put(null, "split", new Split(globalObject), false);
        proto.put(null, "substring", new Substring(globalObject), false);
        proto.put(null, "toLowerCase", new ToLowerCase(globalObject), false);
        proto.put(null, "toUpperCase", new ToUpperCase(globalObject), false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self != Types.UNDEFINED) {
            // Constructor
            if (args[0] != Types.UNDEFINED) {
                PrimitiveDynObject primSelf = (PrimitiveDynObject) self;
                primSelf.setPrimitiveValue(Types.toString(context, args[0]));
                return primSelf;
            } else {
                return self;
            }
        } else {
            // As function
            return Types.toString(context, args[0]);
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynString(context.getGlobalObject());
    }

}
