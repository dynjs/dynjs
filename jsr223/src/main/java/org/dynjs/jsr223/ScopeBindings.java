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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import org.dynjs.api.Scope;

public class ScopeBindings implements Bindings {

	private final Scope scope;

	public ScopeBindings(Scope scope) {
		this.scope = scope;
	}

	@Override
	public void clear() {

	}

	@Override
	public boolean containsValue(Object arg0) {
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}

	@Override
	public boolean containsKey(Object arg0) {
		return false;
	}

	@Override
	public Object get(Object arg0) {
		return null;
	}

	@Override
	public Object put(String name, Object arg1) {
		Object oldValue = scope.resolve(name);
		scope.define(name, arg1);
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> arg0) {
	}

	@Override
	public Object remove(Object arg0) {
		return null;
	}

}