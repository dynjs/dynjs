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
    private JSObject objectPrototype;

    public GlobalObject(DynJS runtime) {
        super();
        this.runtime = runtime;
        this.blockManager = new BlockManager();

        defineReadOnlyGlobalProperty("__throwTypeError", new ThrowTypeError(this), false);

        // ----------------------------------------
        // Built-in types
        // ----------------------------------------

        registerBuiltinType("Object", new BuiltinObject(this));

        this.objectPrototype = getPrototypeFor("Object");
        setPrototype(this.objectPrototype);

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

        defineReadOnlyGlobalProperty("undefined", Types.UNDEFINED, false);

        defineGlobalProperty("parseFloat", new ParseFloat(this), true);
        defineGlobalProperty("parseInt", new ParseInt(this), true);
        defineGlobalProperty("eval", new Eval(this), true);
        defineGlobalProperty("isNaN", new IsNaN(this), true);
        defineGlobalProperty("isFinite", new IsFinite(this), true);

        defineGlobalProperty("encodeURI", new EncodeUri(this), true);
        defineGlobalProperty("decodeURI", new DecodeUri(this), true);
        defineGlobalProperty("encodeURIComponent", new EncodeUriComponent(this), true);
        defineGlobalProperty("decodeURIComponent", new DecodeUriComponent(this), true);

        if (runtime.getConfig().isCommonJSCompatible()) {
            defineGlobalProperty("require", new Require(this), true);
        }
        defineGlobalProperty("include", new Include(this), true);
        defineGlobalProperty("load", new Include(this), true); // hackety hack
        defineGlobalProperty("escape", new Escape(this), true);
        defineGlobalProperty("unescape", new Unescape(this), true);
        defineGlobalProperty("print", new Print(this), true);
        defineGlobalProperty("dynjs", new DynJSBuiltin(this.runtime), true);

        // ----------------------------------------
        // Built-in global objects
        // ----------------------------------------

        put(null, "JSON", new JSON(this), false);
        defineGlobalProperty("Math", new Math(this), true);
        defineGlobalProperty("Intl", new Intl(this), true);

        // ----------------------------------------
        // Java integration
        // ----------------------------------------

        defineGlobalProperty("Packages", new JavaPackage(this, null), true );
        defineGlobalProperty("java",     new JavaPackage(this, "java"), true);
        defineGlobalProperty("javax",    new JavaPackage(this, "javax"), true);
        defineGlobalProperty("org",      new JavaPackage(this, "org"), true);
        defineGlobalProperty("com",      new JavaPackage(this, "com"), true);
        defineGlobalProperty("io",       new JavaPackage(this, "io"), true);

        defineGlobalProperty("System",   System.class, true);
    }

    public JSObject getObjectPrototype() {
        return this.objectPrototype;
    }

    private void registerBuiltinType(String name, final AbstractBuiltinType type) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(type, true, true, true), false);
        defineOwnProperty(null, "__Builtin_" + name,
                PropertyDescriptor.newDataPropertyDescriptor(type, true, true, false), false);
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
                PropertyDescriptor.newDataPropertyDescriptor(value, true, true, true), false);
    }

    public void defineGlobalProperty(final String name, final Object value, boolean enumerable) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, true, true, enumerable), false);
    }

    public void defineReadOnlyGlobalProperty(final String name, final Object value, boolean enumerable) {
        defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, false, false, enumerable), false);
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
