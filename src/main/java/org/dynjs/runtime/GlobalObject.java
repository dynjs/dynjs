package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.builtins.Eval;
import org.dynjs.runtime.builtins.IsFinite;
import org.dynjs.runtime.builtins.IsNaN;
import org.dynjs.runtime.builtins.ParseFloat;
import org.dynjs.runtime.builtins.ParseInt;
import org.dynjs.runtime.modules.ModuleProvider;

public class GlobalObject extends DynObject {

    private DynJS runtime;
    private JSCompiler compiler;
    private BlockManager blockManager;

    public GlobalObject(DynJS runtime) {
        this.runtime = runtime;
        this.compiler = new JSCompiler(runtime.getConfig());
        this.blockManager = new BlockManager();
        
        defineGlobalProperty("Object", new DynObject() );

        defineGlobalProperty("undefined", Types.UNDEFINED);
        defineGlobalProperty("NaN", Double.NaN);
        defineGlobalProperty("Infinity", Double.POSITIVE_INFINITY);
        defineGlobalProperty("-Infinity", Double.NEGATIVE_INFINITY);
        defineGlobalProperty("parseFloat", new ParseFloat(this));
        defineGlobalProperty("parseInt", new ParseInt(this));
        defineGlobalProperty("eval", new Eval(this));
        defineGlobalProperty("isNaN", new IsNaN(this));
        defineGlobalProperty("isFinite", new IsFinite(this));
        defineGlobalProperty("Number", new DynNumber(this));

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
        // TODO: wire me back up.
        return Collections.emptyList();
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

    public Config getConfig() {
        return getRuntime().getConfig();
    }

    public JSCompiler getCompiler() {
        return this.compiler;
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

}
