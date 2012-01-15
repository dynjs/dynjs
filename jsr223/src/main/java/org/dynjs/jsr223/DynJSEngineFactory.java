/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.jsr223;

import static java.util.Arrays.asList;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class DynJSEngineFactory implements ScriptEngineFactory {

	private static final List<String> EXTENSIONS = asList("js");
	private static final List<String> MIME_TYPES = asList(
			"application/x-javascript", "application/javascript",
			"application/ecmascript", "text/javascript", "text/ecmascript");
	private static final String ENGINE_NAME = "dynjs";
	private static final List<String> ENGINE_NAMES = asList(ENGINE_NAME);
	private static final String LANG_NAME = "ECMAScript";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getEngineName()
	 */
	@Override
	public String getEngineName() {
		return ENGINE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getEngineVersion()
	 */
	@Override
	public String getEngineVersion() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getExtensions()
	 */
	@Override
	public List<String> getExtensions() {
		return EXTENSIONS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getMimeTypes()
	 */
	@Override
	public List<String> getMimeTypes() {
		return MIME_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getNames()
	 */
	@Override
	public List<String> getNames() {
		return ENGINE_NAMES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getLanguageName()
	 */
	@Override
	public String getLanguageName() {
		return LANG_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getLanguageVersion()
	 */
	@Override
	public String getLanguageVersion() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
	 */
	@Override
	public Object getParameter(String key) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.script.ScriptEngineFactory#getMethodCallSyntax(java.lang.String,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.script.ScriptEngineFactory#getOutputStatement(java.lang.String)
	 */
	@Override
	public String getOutputStatement(String toDisplay) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getProgram(java.lang.String[])
	 */
	@Override
	public String getProgram(String... statements) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getScriptEngine()
	 */
	@Override
	public ScriptEngine getScriptEngine() {
		return new DynJSEngine(this);
	}

}
