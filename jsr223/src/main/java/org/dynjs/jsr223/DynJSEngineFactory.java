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
	private static final List<String> MIME_TYPES = asList("application/x-javascript", "application/javascript", "application/ecmascript", "text/javascript",
			"text/ecmascript");
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
		return EXTENSIONS;
	}

	@Override
	public List<String> getMimeTypes() {
		return MIME_TYPES;
	}

	@Override
	public List<String> getNames() {
		return ENGINE_NAMES;
	}

	@Override
	public String getLanguageName() {
		return null;
	}

	@Override
	public String getLanguageVersion() {
		return null;
	}

	@Override
	public Object getParameter(String key) {
		return null;
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		return null;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		return null;
	}

	@Override
	public String getProgram(String... statements) {
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new DynJSEngine(this);
	}

}
