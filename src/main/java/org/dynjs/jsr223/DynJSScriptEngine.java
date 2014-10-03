package org.dynjs.jsr223;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.DynJSBuiltin;

import javax.script.*;
import java.io.Reader;

/**
 * @author Bob McWhirter
 */
public class DynJSScriptEngine implements ScriptEngine {

    private final DynJSScriptEngineFactory factory;
    private DynJSScriptContext context;

    public DynJSScriptEngine(DynJSScriptEngineFactory factory) {
        this.factory = factory;
        this.context = new DynJSScriptContext(this);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval( script, context, null );
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return eval( reader, context, null );
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return eval( script, this.context, null );
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return eval( reader, this.context, null );
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return eval( script, this.context, n );
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return eval( reader, this.context, n );
    }

    protected Object eval(String script, ScriptContext context, Bindings bindings) throws ScriptException {
        if ( ! ( context instanceof DynJSScriptContext )  ) {
            throw new IllegalArgumentException( "context must be an instance of " + DynJSScriptContext.class.getName() );
        }

        DynJSScriptContext dynjsContext = (DynJSScriptContext) context;
        ScriptEngineGlobalObject global = getGlobalObject(dynjsContext, bindings);
        DynJS runtime = getRuntime( global, dynjsContext );

        try {
            return runtime.newRunner().withSource(script).execute();
        } catch (Exception t) {
            throw new ScriptException(t);
        }
    }

    protected Object eval(Reader reader, ScriptContext context, Bindings bindings) throws ScriptException {
        if ( ! ( context instanceof DynJSScriptContext )  ) {
            throw new IllegalArgumentException( "context must be an instance of " + DynJSScriptContext.class.getName() );
        }

        DynJSScriptContext dynjsContext = (DynJSScriptContext) context;
        ScriptEngineGlobalObject global = getGlobalObject(dynjsContext, bindings);
        DynJS runtime = getRuntime( global, dynjsContext );

        try {
            return runtime.newRunner().withSource(reader).execute();
        } catch (Exception t) {
            throw new ScriptException(t);
        }
    }

    private ScriptEngineGlobalObject getGlobalObject(DynJSScriptContext context, Bindings localBindings) {
        if ( localBindings == null ) {
            return context.getGlobalObject();
        }

        return new ScriptEngineGlobalObject( context.getGlobalObject(), localBindings );
    }

    private DynJS getRuntime(ScriptEngineGlobalObject global, DynJSScriptContext context) {
        Object builtin = global.get(null, "dynjs");

        DynJS runtime = null;

        if ( builtin != Types.UNDEFINED ) {
            runtime = ((DynJSBuiltin) builtin).getRuntime();
        }

        runtime = new DynJS( new Config(), global );

        Object argv = context.getAttribute( ScriptEngine.ARGV );
        if ( argv != null ) {
            if ( ! argv.getClass().isArray() ) {
                throw new IllegalArgumentException( "ARGV must be an array" );
            }
            runtime.getConfig().setArgv((Object[]) argv);
        }

        return runtime;
    }

    @Override
    public void put(String key, Object value) {
        getBindings( ScriptContext.ENGINE_SCOPE ).put( key, value );
    }

    @Override
    public Object get(String key) {
        return getBindings( ScriptContext.ENGINE_SCOPE ).get( key );
    }

    @Override
    public Bindings getBindings(int scope) {
        return this.context.getBindings( scope );
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        this.context.setBindings( bindings, scope );
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptContext getContext() {
        return this.context;
    }

    @Override
    public void setContext(ScriptContext context) {
        if ( ! ( context instanceof DynJSScriptContext ) ) {
            throw new IllegalArgumentException( "Context must be " + DynJSScriptContext.class.getName() );
        }
        this.context = (DynJSScriptContext) context;
    }

    @Override
    public DynJSScriptEngineFactory getFactory() {
        return this.factory;
    }

}
