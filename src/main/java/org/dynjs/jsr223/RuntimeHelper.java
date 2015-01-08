package org.dynjs.jsr223;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.DynJSBuiltin;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

/**
 * @author Bob McWhirter
 */
public class RuntimeHelper {

    public static ScriptEngineGlobalObject getGlobalObject(ScriptContext context) {
        ScriptEngineGlobalObject global = (ScriptEngineGlobalObject) context.getAttribute(DynJSScriptEngine.GLOBAL_OBJECT);

        if( global == null ) {
            global = new ScriptEngineGlobalObject(context);
            context.setAttribute( DynJSScriptEngine.GLOBAL_OBJECT, global, ScriptContext.ENGINE_SCOPE );
        }

        return global;
    }

    public static  DynJS getRuntime(ScriptEngineGlobalObject global, ScriptContext context) {
        Object builtin = global.get(null, "dynjs");

        DynJS runtime = null;

        if ( builtin != Types.UNDEFINED ) {
            runtime = ((DynJSBuiltin) builtin).getRuntime();
        } else {
            runtime = new DynJS(new Config(), global);
        }

        Object argv = context.getAttribute( ScriptEngine.ARGV );
        if ( argv != null ) {
            if ( ! argv.getClass().isArray() ) {
                throw new IllegalArgumentException( "ARGV must be an array" );
            }
            runtime.getConfig().setArgv((Object[]) argv);
        }

        return runtime;
    }
}
