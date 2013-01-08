package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
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
import org.dynjs.runtime.builtins.types.string.prototype.Replace;
import org.dynjs.runtime.builtins.types.string.prototype.Search;
import org.dynjs.runtime.builtins.types.string.prototype.Slice;
import org.dynjs.runtime.builtins.types.string.prototype.Split;
import org.dynjs.runtime.builtins.types.string.prototype.Substr;
import org.dynjs.runtime.builtins.types.string.prototype.Substring;
import org.dynjs.runtime.builtins.types.string.prototype.ToLocaleLowerCase;
import org.dynjs.runtime.builtins.types.string.prototype.ToLocaleUpperCase;
import org.dynjs.runtime.builtins.types.string.prototype.ToLowerCase;
import org.dynjs.runtime.builtins.types.string.prototype.ToString;
import org.dynjs.runtime.builtins.types.string.prototype.ToUpperCase;
import org.dynjs.runtime.builtins.types.string.prototype.Trim;
import org.dynjs.runtime.builtins.types.string.prototype.ValueOf;

public class BuiltinString extends AbstractBuiltinType {

    public BuiltinString(final GlobalObject globalObject) {
        super(globalObject, "value");

        final DynString proto = new DynString(globalObject, "");
        setPrototypeProperty(proto);
    }

    public void initialize(GlobalObject globalObject, JSObject proto) {
        // String.foo()
        defineNonEnumerableProperty(this, "fromCharCode", new FromCharCode(globalObject) );
        
        // String.prototype.foo()
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        defineNonEnumerableProperty(proto, "constructor", this );
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject) );
        defineNonEnumerableProperty(proto, "charAt", new CharAt(globalObject) );
        defineNonEnumerableProperty(proto, "charCodeAt", new CharCodeAt(globalObject) );
        defineNonEnumerableProperty(proto, "concat", new Concat(globalObject) );
        defineNonEnumerableProperty(proto, "indexOf", new IndexOf(globalObject) );
        defineNonEnumerableProperty(proto, "lastIndexOf", new LastIndexOf(globalObject) );
        defineNonEnumerableProperty(proto, "localeCompare", new LocaleCompare(globalObject) );
        defineNonEnumerableProperty(proto, "match", new Match(globalObject) );
        defineNonEnumerableProperty(proto, "search", new Search(globalObject) );
        defineNonEnumerableProperty(proto, "slice", new Slice(globalObject) );
        defineNonEnumerableProperty(proto, "split", new Split(globalObject) );
        defineNonEnumerableProperty(proto, "substring", new Substring(globalObject) );
        defineNonEnumerableProperty(proto, "substr", new Substr(globalObject) ); // Alias, 'cause node likes this
        defineNonEnumerableProperty(proto, "toLowerCase", new ToLowerCase(globalObject) );
        defineNonEnumerableProperty(proto, "toUpperCase", new ToUpperCase(globalObject) );
        defineNonEnumerableProperty(proto, "toLocaleLowerCase", new ToLocaleLowerCase(globalObject) );
        defineNonEnumerableProperty(proto, "toLocaleUpperCase", new ToLocaleUpperCase(globalObject) );
        defineNonEnumerableProperty(proto, "trim", new Trim(globalObject) );
        defineNonEnumerableProperty(proto, "replace", new Replace(globalObject) ); // http://es5.github.com/#x15.5.4.11
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");
        
        if (self != Types.UNDEFINED && self != Types.NULL ) {
            // Constructor
            String value = "";
            if (numArgs != 0 ) {
                value = Types.toString( context, args[0] );
            }
            PrimitiveDynObject primSelf = (PrimitiveDynObject) self;
            primSelf.setPrimitiveValue(value);
            return primSelf;
        } else {
            // As function
            if ( numArgs == 0 ) {
                return "";
            }
            return Types.toString(context, args[0]);
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynString(context.getGlobalObject());
    }

}
