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

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.apache.commons.io.input.ReaderInputStream;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

final class DynJSEngine extends AbstractScriptEngine {

	private final ScriptEngineFactory scriptEngineFactory;

	public DynJSEngine(ScriptEngineFactory scriptEngineFactory) {
		super();
		this.scriptEngineFactory = scriptEngineFactory;
	}

	@Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		DynJS dynJS = new DynJS();
		dynJS.eval(new DynThreadContext(), script);
		return null;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		DynJS dynJS = new DynJS();
		dynJS.eval(new DynThreadContext(), new ReaderInputStream(reader));
		return null;
	}

	@Override
	public Bindings createBindings() {
		return null;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return scriptEngineFactory;
	}
}