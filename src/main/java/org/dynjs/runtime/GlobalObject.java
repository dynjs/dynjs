package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.builtins.DecodeUri;
import org.dynjs.runtime.builtins.EncodeUri;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.IsFinite;
import org.dynjs.runtime.builtins.IsNaN;
import org.dynjs.runtime.builtins.JSON;
import org.dynjs.runtime.builtins.Math;
import org.dynjs.runtime.builtins.ParseFloat;
import org.dynjs.runtime.builtins.ParseInt;
import org.dynjs.runtime.builtins.Require;
import org.dynjs.runtime.builtins.ThrowTypeError;
import org.dynjs.runtime.builtins.types.AbstractBuiltinType;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinBoolean;
import org.dynjs.runtime.builtins.types.BuiltinError;
import org.dynjs.runtime.builtins.types.BuiltinFunction;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
import org.dynjs.runtime.builtins.types.BuiltinObject;
import org.dynjs.runtime.builtins.types.BuiltinRangeError;
import org.dynjs.runtime.builtins.types.BuiltinReferenceError;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;
import org.dynjs.runtime.builtins.types.BuiltinString;
import org.dynjs.runtime.builtins.types.BuiltinSyntaxError;
import org.dynjs.runtime.builtins.types.BuiltinTypeError;
import org.dynjs.runtime.builtins.types.BuiltinURIError;
import org.dynjs.runtime.modules.ConsoleModule;
import org.dynjs.runtime.modules.FilesystemModuleProvider;
import org.dynjs.runtime.modules.JavaClassModuleProvider;
import org.dynjs.runtime.modules.ModuleProvider;

public class GlobalObject extends DynObject {

    private DynJS runtime;
    private BlockManager blockManager;
    private List<ModuleProvider> moduleProviders = new ArrayList<>();
    private List<String> loadPaths = new ArrayList<>();
    private List<AbstractBuiltinType> builtinTypes = new ArrayList<>();

    public GlobalObject(DynJS runtime) {
        super(null);
        this.runtime = runtime;
        this.blockManager = new BlockManager();
        
        // ----------------------------------------
        // Built-in types
        // ----------------------------------------

        registerBuiltinType("Object", new BuiltinObject(this));
        registerBuiltinType("Function", new BuiltinFunction(this));
        registerBuiltinType("Boolean", new BuiltinBoolean(this));
        registerBuiltinType("Number", new BuiltinNumber(this));
        registerBuiltinType("Array", new BuiltinArray(this));
        registerBuiltinType("String", new BuiltinString(this));
        registerBuiltinType("RegExp", new BuiltinRegExp(this));
        registerBuiltinType("Error", new BuiltinError(this));
        registerBuiltinType("ReferenceError", new BuiltinReferenceError(this));
        registerBuiltinType("RangeError", new BuiltinRangeError(this));
        registerBuiltinType("SyntaxError", new BuiltinSyntaxError(this));
        registerBuiltinType("TypeError", new BuiltinTypeError(this));
        registerBuiltinType("URIError", new BuiltinURIError(this));
        
        initializeBuiltinTypes();

        // ----------------------------------------
        // Built-in global functions
        // ----------------------------------------
        
        put(null, "__throwTypeError", new ThrowTypeError(this), false);
        put(null, "undefined", Types.UNDEFINED, false);
        put(null, "parseFloat", new ParseFloat(this), false);
        put(null, "parseInt", new ParseInt(this), false);
        put(null, "eval", new Eval(this), false);
        put(null, "isNaN", new IsNaN(this), false);
        put(null, "isFinite", new IsFinite(this), false);
        put(null, "require", new Require(this), false);
        
        put(null, "encodeURI", new EncodeUri(this), false);
        put(null, "decodeURI", new DecodeUri(this), false);
        
        // ----------------------------------------
        // Built-in global objects
        // ----------------------------------------

        put(null, "JSON", new JSON(this), false);
        put(null, "Math", new Math(this), false);
        
        // ----------------------------------------
        // Module-provider setup
        // ----------------------------------------
        
        this.moduleProviders.add(new FilesystemModuleProvider(this));

        JavaClassModuleProvider javaClassModuleProvider = new JavaClassModuleProvider();
        javaClassModuleProvider.addModule(new ConsoleModule());

        this.moduleProviders.add(javaClassModuleProvider);

        setPrototype(getPrototypeFor("Object"));

    }

    private void registerBuiltinType(String name, AbstractBuiltinType type) {
        put(null, name, type, false);
        this.builtinTypes.add( type );
    }
    
    private void initializeBuiltinTypes() {
        for ( AbstractBuiltinType each : this.builtinTypes ) {
            each.setPrototype( getPrototypeFor( "Function" ));
            each.initialize( this );
        }
    }

    public static GlobalObject newGlobalObject(DynJS runtime) {
        return runtime.getConfig().getGlobalObjectFactory().newGlobalObject(runtime);
    }

    public List<ModuleProvider> getModuleProviders() {
        return this.moduleProviders;
    }

    public void addLoadPath(String path) {
        this.loadPaths.add(path);
    }

    public List<String> getLoadPaths() {
        return this.loadPaths;
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

    public Config getConfig() {
        return getRuntime().getConfig();
    }

    public JSCompiler getCompiler() {
        return this.runtime.getCompiler();
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public Entry retrieveBlockEntry(int statementNumber) {
        return this.blockManager.retrieve(statementNumber);
    }

    public void defineGlobalProperty(final String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", true);
                set("Configurable", true);
                set("Enumerable", true);
            }
        };
        defineOwnProperty(null, name, desc, false);
    }

    public JSObject getPrototypeFor(String type) {
        Object typeObj = get(null, type);
        if (typeObj == Types.UNDEFINED) {
            return null;
        }
        Object prototype = ((JSObject) typeObj).get(null, "prototype");
        if (prototype == Types.UNDEFINED) {
            return null;
        }
        return (JSObject) prototype;
    }

}
