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
        this.forceDefineNonEnumerableProperty( "fromCharCode", new FromCharCode(globalObject) );
        
        // String.prototype.foo()
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        proto.forceDefineNonEnumerableProperty("constructor", this );
        proto.forceDefineNonEnumerableProperty("toString", new ToString(globalObject) );
        proto.forceDefineNonEnumerableProperty("valueOf", new ValueOf(globalObject) );
        proto.forceDefineNonEnumerableProperty("charAt", new CharAt(globalObject) );
        proto.forceDefineNonEnumerableProperty("charCodeAt", new CharCodeAt(globalObject) );
        proto.forceDefineNonEnumerableProperty("concat", new Concat(globalObject) );
        proto.forceDefineNonEnumerableProperty("indexOf", new IndexOf(globalObject) );
        proto.forceDefineNonEnumerableProperty("lastIndexOf", new LastIndexOf(globalObject) );
        proto.forceDefineNonEnumerableProperty("localeCompare", new LocaleCompare(globalObject) );
        proto.forceDefineNonEnumerableProperty("match", new Match(globalObject) );
        proto.forceDefineNonEnumerableProperty("search", new Search(globalObject) );
        proto.forceDefineNonEnumerableProperty("slice", new Slice(globalObject) );
        proto.forceDefineNonEnumerableProperty("split", new Split(globalObject) );
        proto.forceDefineNonEnumerableProperty("substring", new Substring(globalObject) );
        proto.forceDefineNonEnumerableProperty("substr", new Substr(globalObject) ); // Alias, 'cause node likes this
        proto.forceDefineNonEnumerableProperty("toLowerCase", new ToLowerCase(globalObject) );
        proto.forceDefineNonEnumerableProperty("toUpperCase", new ToUpperCase(globalObject) );
        proto.forceDefineNonEnumerableProperty("toLocaleLowerCase", new ToLocaleLowerCase(globalObject) );
        proto.forceDefineNonEnumerableProperty("toLocaleUpperCase", new ToLocaleUpperCase(globalObject) );
        proto.forceDefineNonEnumerableProperty("trim", new Trim(globalObject) );
        proto.forceDefineNonEnumerableProperty( "replace", new Replace(globalObject) ); // http://es5.github.com/#x15.5.4.11
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
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: String>";
    }
    
}
