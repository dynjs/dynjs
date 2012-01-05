package org.dynjs.jsr223;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

final class DynJSEngine extends AbstractScriptEngine {

	private final ScriptEngineFactory scriptEngineFactory;

	public DynJSEngine(ScriptEngineFactory scriptEngineFactory) {
		super();
		this.scriptEngineFactory = scriptEngineFactory;
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bindings createBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return scriptEngineFactory;
	}
}