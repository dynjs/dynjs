package org.dynjs.jsr223;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.DynJSBuiltin;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * @author Bob McWhirter
 */
public class DynJSCompiledScript extends CompiledScript {

    private final DynJSScriptEngine engine;
    private final JSProgram program;

    DynJSCompiledScript(DynJSScriptEngine engine, JSProgram program) {
        this.engine = engine;
        this.program = program;
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject( context );
        DynJS runtime = RuntimeHelper.getRuntime( global, context );
        return runtime.newRunner().withSource( this.program ).execute();
    }

    @Override
    public ScriptEngine getEngine() {
        return this.engine;
    }

}
