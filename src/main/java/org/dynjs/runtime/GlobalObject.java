package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.builtins.ClasspathModuleProvider;
import org.dynjs.runtime.builtins.DecodeUri;
import org.dynjs.runtime.builtins.DecodeUriComponent;
import org.dynjs.runtime.builtins.DynJSBuiltin;
import org.dynjs.runtime.builtins.EncodeUri;
import org.dynjs.runtime.builtins.EncodeUriComponent;
import org.dynjs.runtime.builtins.Escape;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.Include;
import org.dynjs.runtime.builtins.Intl;
import org.dynjs.runtime.builtins.IsFinite;
import org.dynjs.runtime.builtins.IsNaN;
import org.dynjs.runtime.builtins.JSON;
import org.dynjs.runtime.builtins.Math;
import org.dynjs.runtime.builtins.ParseFloat;
import org.dynjs.runtime.builtins.ParseInt;
import org.dynjs.runtime.builtins.Print;
import org.dynjs.runtime.builtins.Require;
import org.dynjs.runtime.builtins.ThrowTypeError;
import org.dynjs.runtime.builtins.Unescape;
import org.dynjs.runtime.builtins.types.AbstractBuiltinType;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinBoolean;
import org.dynjs.runtime.builtins.types.BuiltinDate;
import org.dynjs.runtime.builtins.types.BuiltinError;
import org.dynjs.runtime.builtins.types.BuiltinEvalError;
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
import org.dynjs.runtime.java.JavaPackage;
import org.dynjs.runtime.modules.ConsoleModule;
import org.dynjs.runtime.modules.FilesystemModuleProvider;
import org.dynjs.runtime.modules.JavaClassModuleProvider;
import org.dynjs.runtime.modules.ModuleProvider;
import org.dynjs.runtime.modules.UtilModule;

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

        defineReadOnlyGlobalProperty("__throwTypeError", new ThrowTypeError(this));

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
        registerBuiltinType("Date", new BuiltinDate(this));
        registerBuiltinType("Error", new BuiltinError(this));
        registerBuiltinType("ReferenceError", new BuiltinReferenceError(this));
        registerBuiltinType("RangeError", new BuiltinRangeError(this));
        registerBuiltinType("SyntaxError", new BuiltinSyntaxError(this));
        registerBuiltinType("TypeError", new BuiltinTypeError(this));
        registerBuiltinType("URIError", new BuiltinURIError(this));
        registerBuiltinType("EvalError", new BuiltinEvalError(this));

        initializeBuiltinTypes();

        // ----------------------------------------
        // Built-in global functions
        // ----------------------------------------

        defineReadOnlyGlobalProperty("undefined", Types.UNDEFINED);

        defineGlobalProperty("parseFloat", new ParseFloat(this));
        defineGlobalProperty("parseInt", new ParseInt(this));
        defineGlobalProperty("eval", new Eval(this));
        defineGlobalProperty("isNaN", new IsNaN(this));
        defineGlobalProperty("isFinite", new IsFinite(this));

        defineGlobalProperty("encodeURI", new EncodeUri(this));
        defineGlobalProperty("decodeURI", new DecodeUri(this));
        defineGlobalProperty("encodeURIComponent", new EncodeUriComponent(this));
        defineGlobalProperty("decodeURIComponent", new DecodeUriComponent(this));

        if (runtime.getConfig().isNodePackageManagerEnabled()) {
            defineGlobalProperty("require", new Require(this));
        }
        defineGlobalProperty("include", new Include(this));
        defineGlobalProperty("load", new Include(this)); // hackety hack
        defineGlobalProperty("escape", new Escape(this));
        defineGlobalProperty("unescape", new Unescape(this));
        defineGlobalProperty("print", new Print(this));
        defineGlobalProperty("dynjs", new DynJSBuiltin(this.runtime));

        // ----------------------------------------
        // Built-in global objects
        // ----------------------------------------

        put(null, "JSON", new JSON(this), false);
        defineGlobalProperty("Math", new Math(this));
        defineGlobalProperty("Intl", new Intl(this));

        // ----------------------------------------
        // Module-provider setup
        // ----------------------------------------

        this.moduleProviders.add(new FilesystemModuleProvider(this));
        this.moduleProviders.add(new ClasspathModuleProvider());

        JavaClassModuleProvider javaClassModuleProvider = new JavaClassModuleProvider();
        javaClassModuleProvider.addModule(new ConsoleModule());
        javaClassModuleProvider.addModule(new UtilModule());

        this.moduleProviders.add(javaClassModuleProvider);

        // ----------------------------------------
        // Java integration
        // ----------------------------------------

        defineGlobalProperty("java", new JavaPackage(this, "java"));
        defineGlobalProperty("org", new JavaPackage(this, "org"));
        defineGlobalProperty("com", new JavaPackage(this, "com"));

        setPrototype(getPrototypeFor("Object"));

    }

    private void registerBuiltinType(String name, final AbstractBuiltinType type) {
        defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set("Value", type);
                set("Enumerable", false);
                set("Writable", true);
                set("Configurable", true);
            }
        }, false);
        put(null, "__Builtin_" + name, type, false);
        this.builtinTypes.add(type);
    }

    private void initializeBuiltinTypes() {
        for (AbstractBuiltinType each : this.builtinTypes) {
            each.setPrototype(getPrototypeFor("Function"));
            each.initialize(this);
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

    public org.dynjs.runtime.BlockManager.Entry retrieveBlockEntry(int statementNumber) {
        return this.blockManager.retrieve(statementNumber);
    }

    public void defineGlobalProperty(final String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", true);
                set("Enumerable", false);
                set("Configurable", true);
            }
        };
        defineOwnProperty(null, name, desc, false);
    }

    public void defineReadOnlyGlobalProperty(final String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
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
