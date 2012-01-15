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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import org.dynjs.api.Scope;

public class ScopeBindings implements Bindings {

	private final Map<String, Object> properties;

	/**
	 * Default constructor
	 */
	public ScopeBindings() {
		properties = new HashMap<>();
	}

	/**
	 * Used to wrap bindings
	 * 
	 * @param bindings
	 */
	private ScopeBindings(Bindings bindings) {
		properties = bindings;
	}

	@Override
	public void clear() {
		properties.clear();
	}

	@Override
	public boolean containsValue(Object value) {
		return properties.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return properties.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return properties.keySet();
	}

	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public Collection<Object> values() {
		return properties.values();
	}

	@Override
	public boolean containsKey(Object key) {
		return properties.containsKey(key);
	}

	@Override
	public Object get(Object key) {
		return properties.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return properties.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> arg0) {
		properties.putAll(arg0);
	}

	@Override
	public Object remove(Object key) {
		return properties.remove(key);
	}

	public Scope asScope() {
		return new Scope() {

			@Override
			public Scope getEnclosingScope() {
				return null;
			}

			@Override
			public Object resolve(String name) {
				return properties.get(name);
			}

			@Override
			public void define(String property, Object value) {
				properties.put(property, value);
			}
		};
	}

	public Scope asScope(final Scope enclosingScope) {
		return new Scope() {

			@Override
			public Scope getEnclosingScope() {
				return enclosingScope;
			}

			@Override
			public Object resolve(String name) {
				return properties.get(name);
			}

			@Override
			public void define(String property, Object value) {
				properties.put(property, value);
			}
		};
	}

	public static ScopeBindings wrap(Bindings bindings) {
		return new ScopeBindings(bindings);
	}
}