package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
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

    public BuiltinString(final GlobalContext globalContext) {
        super(globalContext, "value");

        final DynString proto = new DynString(globalContext, "");
        setPrototypeProperty(proto);
    }

    public void initialize(GlobalContext globalContext, JSObject proto) {
        // String.foo()
        defineNonEnumerableProperty(this, "fromCharCode", new FromCharCode(globalContext) );
        
        // String.prototype.foo()
        proto.setPrototype(globalContext.getPrototypeFor("Object"));
        defineNonEnumerableProperty(proto, "constructor", this );
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalContext) );
        defineNonEnumerableProperty(proto, "charAt", new CharAt(globalContext) );
        defineNonEnumerableProperty(proto, "charCodeAt", new CharCodeAt(globalContext) );
        defineNonEnumerableProperty(proto, "concat", new Concat(globalContext) );
        defineNonEnumerableProperty(proto, "indexOf", new IndexOf(globalContext) );
        defineNonEnumerableProperty(proto, "lastIndexOf", new LastIndexOf(globalContext) );
        defineNonEnumerableProperty(proto, "localeCompare", new LocaleCompare(globalContext) );
        defineNonEnumerableProperty(proto, "match", new Match(globalContext) );
        defineNonEnumerableProperty(proto, "search", new Search(globalContext) );
        defineNonEnumerableProperty(proto, "slice", new Slice(globalContext) );
        defineNonEnumerableProperty(proto, "split", new Split(globalContext) );
        defineNonEnumerableProperty(proto, "substring", new Substring(globalContext) );
        defineNonEnumerableProperty(proto, "substr", new Substr(globalContext) ); // Alias, 'cause node likes this
        defineNonEnumerableProperty(proto, "toLowerCase", new ToLowerCase(globalContext) );
        defineNonEnumerableProperty(proto, "toUpperCase", new ToUpperCase(globalContext) );
        defineNonEnumerableProperty(proto, "toLocaleLowerCase", new ToLocaleLowerCase(globalContext) );
        defineNonEnumerableProperty(proto, "toLocaleUpperCase", new ToLocaleUpperCase(globalContext) );
        defineNonEnumerableProperty(proto, "trim", new Trim(globalContext) );
        defineNonEnumerableProperty(proto, "replace", new Replace(globalContext) ); // http://es5.github.com/#x15.5.4.11
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
        return new DynString(context.getGlobalContext());
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: String>";
    }
    
}
