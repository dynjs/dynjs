package org.dynjs.jsr223;

import static java.util.Arrays.asList;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class DynJSEngineFactory implements ScriptEngineFactory {

	@Override
	public String getEngineName() {
		// TODO Auto-generated method stub
		return "dynjs";
	}

	@Override
	public String getEngineVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMimeTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getNames() {
		return asList("dynjs");
	}

	@Override
	public String getLanguageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLanguageVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParameter(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProgram(String... statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new ScriptEngine() {
			
			@Override
			public void setContext(ScriptContext context) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBindings(Bindings bindings, int scope) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void put(String key, Object value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public ScriptEngineFactory getFactory() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ScriptContext getContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Bindings getBindings(int scope) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object get(String key) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(Reader reader, Bindings n) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(String script, Bindings n) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(Reader reader, ScriptContext context) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(String script, ScriptContext context) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(Reader reader) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object eval(String script) throws ScriptException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Bindings createBindings() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
