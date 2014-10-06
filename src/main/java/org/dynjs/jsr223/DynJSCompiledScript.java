package org.dynjs.jsr223;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.Runner;
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
    private String filename;

    DynJSCompiledScript(DynJSScriptEngine engine, JSProgram program) {
        this.engine = engine;
        this.program = program;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename(){
        return this.filename;
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        ScriptEngineGlobalObject global = RuntimeHelper.getGlobalObject(context);
        DynJS runtime = RuntimeHelper.getRuntime(global, context);
        Runner runner = runtime.newRunner();
        runner.withSource( this.program );
        String filename = (String) context.getAttribute( ScriptEngine.FILENAME );

        if ( filename == null ) {
            filename = this.filename;
        }

        if ( filename != null ) {
            runner.withFileName( filename );
        }

        return runner.execute();
    }

    @Override
    public ScriptEngine getEngine() {
        return this.engine;
    }

}
