package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.IsFinite;
import org.dynjs.runtime.builtins.IsNaN;
import org.dynjs.runtime.builtins.ParseFloat;
import org.dynjs.runtime.builtins.ParseInt;
import org.dynjs.runtime.builtins.Require;
import org.dynjs.runtime.builtins.ThrowTypeError;
import org.dynjs.runtime.builtins.types.BuiltinArray;
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

    public GlobalObject(DynJS runtime) {
        this.runtime = runtime;
        this.blockManager = new BlockManager();
        
        defineGlobalProperty("Function", new BuiltinFunction(this));
        defineGlobalProperty("Object", new BuiltinObject(this));
        
        defineGlobalProperty("__throwTypeError", new ThrowTypeError(this));

        defineGlobalProperty("undefined", Types.UNDEFINED);
        defineGlobalProperty("parseFloat", new ParseFloat(this));
        defineGlobalProperty("parseInt", new ParseInt(this));
        defineGlobalProperty("eval", new Eval(this));
        defineGlobalProperty("isNaN", new IsNaN(this));
        defineGlobalProperty("isFinite", new IsFinite(this));
        defineGlobalProperty("Number", new BuiltinNumber(this));
        defineGlobalProperty("Array", new BuiltinArray(this));
        defineGlobalProperty("String", new BuiltinString(this));
        defineGlobalProperty("RegExp", new BuiltinRegExp(this));
        
        defineGlobalProperty("Error", new BuiltinError(this));
        defineGlobalProperty("ReferenceError", new BuiltinReferenceError(this));
        defineGlobalProperty("RangeError", new BuiltinRangeError(this));
        defineGlobalProperty("SyntaxError", new BuiltinSyntaxError(this));
        defineGlobalProperty("TypeError", new BuiltinTypeError(this));
        defineGlobalProperty("URIError", new BuiltinURIError(this));
        
        defineGlobalProperty("require", new Require(this));
        this.moduleProviders.add( new FilesystemModuleProvider() );
        
        JavaClassModuleProvider javaClassModuleProvider = new JavaClassModuleProvider();
        javaClassModuleProvider.addModule( new ConsoleModule() );
        
        this.moduleProviders.add( javaClassModuleProvider );

        /*
         * put("-Infinity", Double.NEGATIVE_INFINITY);
         * put("Object", new DynObject() {{
         * setProperty("defineProperty", new DefineProperty());
         * }});
         * put("Array", new DynObject());
         * put("Date", new DynObject());
         * put("String", new DynObject());
         * put("Boolean", new DynObject());
         * put("Error", new DynObject());
         * put("Function", new DynObject() {{
         * setProperty("prototype", get("Object"));
         * }});
         * put("require", DynJSCompiler.wrapFunction(get("Function"), new
         * Require()));
         * put("Math", new DynObject());
         */
    }

    public static GlobalObject newGlobalObject(DynJS runtime) {
        return runtime.getConfig().getGlobalObjectFactory().newGlobalObject(runtime);
    }

    public List<ModuleProvider> getModuleProviders() {
        return this.moduleProviders;
    }
    
    public void addLoadPath(String path) {
        this.loadPaths.add( path );
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
        if ( typeObj == Types.UNDEFINED ) {
            return null;
        }
        Object prototype = ((JSObject)typeObj).get( null, "prototype" );
        if ( prototype == Types.UNDEFINED ) {
            return null;
        }
        return (JSObject) prototype;
    }

}
