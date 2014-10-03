package org.dynjs.jsr223;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class DynJSScriptContext implements ScriptContext {

    private final static List<Integer> SCOPES;

    static {
        SCOPES = Collections.unmodifiableList(new ArrayList<Integer>() {{
            add( ScriptContext.ENGINE_SCOPE );
            add( ScriptContext.GLOBAL_SCOPE );
        }});
    }

    private final DynJSScriptEngine engine;
    private final ScriptEngineGlobalObject global;
    private Writer writer;
    private Writer errorWriter;
    private Reader reader;

    public DynJSScriptContext(DynJSScriptEngine engine) {
        this.engine = engine;
        this.writer = new OutputStreamWriter(System.out);
        this.errorWriter = new OutputStreamWriter(System.err);
        this.reader = new InputStreamReader(System.in);

        this.global = new ScriptEngineGlobalObject( this );
    }

    ScriptEngineGlobalObject getGlobalObject() {
        return this.global;
    }

    DynJSScriptEngine getEngine() {
        return engine;
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        if (scope == ScriptContext.GLOBAL_SCOPE) {
            this.engine.getFactory().setGlobalBindings(bindings);
            return;
        } else if (scope == ScriptContext.ENGINE_SCOPE) {
            this.global.setBindings( bindings );
            return;
        }

        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override
    public Bindings getBindings(int scope) {
        if (scope == ScriptContext.GLOBAL_SCOPE) {
            return this.engine.getFactory().getGlobalBindings();
        } else if (scope == ScriptContext.ENGINE_SCOPE) {
            return this.global.getBindings();
        }

        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        if (scope == ScriptContext.GLOBAL_SCOPE) {
            this.engine.getFactory().getGlobalBindings().put(name, value);
            return;
        } else if (scope == ScriptContext.ENGINE_SCOPE) {
            this.global.getBindings().put(name, value);
            return;
        }

        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override
    public Object getAttribute(String name, int scope) {
        if (scope == ScriptContext.GLOBAL_SCOPE) {
            return this.engine.getFactory().getGlobalBindings().get(name);
        } else if (scope == ScriptContext.ENGINE_SCOPE) {
            return this.global.getBindings().get(name);
        }
        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override
    public Object removeAttribute(String name, int scope) {
        if (scope == ScriptContext.GLOBAL_SCOPE) {
            return this.engine.getFactory().getGlobalBindings().remove(name);
        } else if (scope == ScriptContext.ENGINE_SCOPE) {
            return this.global.getBindings().remove(name);
        }

        throw new IllegalArgumentException("Invalid scope: " + scope);
    }

    @Override
    public Object getAttribute(String name) {
        Object value = getAttribute(name, ScriptContext.ENGINE_SCOPE);
        if (value == null) {
            value = getAttribute(name, ScriptContext.GLOBAL_SCOPE);
        }
        return value;
    }

    @Override
    public int getAttributesScope(String name) {
        if (getAttribute(name, ScriptContext.ENGINE_SCOPE) != null) {
            return ScriptContext.ENGINE_SCOPE;
        }

        if (getAttribute(name, ScriptContext.GLOBAL_SCOPE) != null) {
            return ScriptContext.GLOBAL_SCOPE;
        }

        return -1;
    }

    @Override
    public Writer getWriter() {
        return this.writer;
    }

    @Override
    public Writer getErrorWriter() {
        return this.errorWriter;
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void setErrorWriter(Writer writer) {
        this.errorWriter = writer;
    }

    @Override
    public Reader getReader() {
        return this.reader;
    }

    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<Integer> getScopes() {
        return SCOPES;
    }
}
