package org.dynjs.jsr223;

import static java.util.Arrays.asList;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class DynJSEngineFactory implements ScriptEngineFactory {

	private static final String ENGINE_NAME = "dynjs";
	private static final List<String> ENGINE_NAMES = asList(ENGINE_NAME);

	@Override
	public String getEngineName() {
		// TODO Auto-generated method stub
		return ENGINE_NAME;
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
		return ENGINE_NAMES;
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
		return new DynJSEngine();
	}

}
