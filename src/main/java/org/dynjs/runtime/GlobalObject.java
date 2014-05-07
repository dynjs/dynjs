package org.dynjs.runtime;

import org.dynjs.runtime.builtins.*;
import org.dynjs.runtime.builtins.Math;
import org.dynjs.runtime.builtins.types.*;
import org.dynjs.runtime.java.JSAdapter;
import org.dynjs.runtime.java.JavaPackage;

import java.util.ArrayList;
import java.util.List;

public class GlobalObject extends DynObject {

    private DynJS runtime;
    private BlockManager blockManager;
    private List<AbstractBuiltinType> builtinTypes = new ArrayList<>();

    public GlobalObject(DynJS runtime) {
        super();
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

        if (runtime.getConfig().isRhinoCompatible()) {
            registerBuiltinType("JSAdapter", new JSAdapter(this));
        }

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

        if (runtime.getConfig().isCommonJSCompatible()) {
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
        // Java integration
        // ----------------------------------------

        defineGlobalProperty("Packages", new JavaPackage(this, null));
        defineGlobalProperty("java",     new JavaPackage(this, "java"));
        defineGlobalProperty("javax",    new JavaPackage(this, "javax"));
        defineGlobalProperty("org",      new JavaPackage(this, "org"));
        defineGlobalProperty("com",      new JavaPackage(this, "com"));
        defineGlobalProperty("io",       new JavaPackage(this, "io"));

        defineGlobalProperty("System",   System.class);

        setPrototype(getPrototypeFor("Object"));

    }

    private void registerBuiltinType(String name, final AbstractBuiltinType type) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(type, true, true, false), false);
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

    public DynJS getRuntime() {
        return this.runtime;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public org.dynjs.runtime.BlockManager.Entry retrieveBlockEntry(int statementNumber) {
        return this.blockManager.retrieve(statementNumber);
    }

    public void defineGlobalProperty(final String name, final Object value) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, true, true, false), false);
    }

    public void defineReadOnlyGlobalProperty(final String name, final Object value) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, false, false, false), false);
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
