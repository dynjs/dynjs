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

import static org.junit.Assert.*;

import org.dynjs.api.Scope;
import org.dynjs.runtime.DynObject;
import org.junit.Test;

public class ScopBindingsTest {

	@Test
	public void should_get_object_put() {
		Scope scope = new DynObject();
		ScopeBindings bindings = new ScopeBindings(scope);
		Object object = bindings.put("a", "a");
		assertNull(object);
		assertEquals("a", scope.resolve("a"));
	}

	@Test
	public void should_return_previously_put() {
		Scope scope = new DynObject();
		ScopeBindings bindings = new ScopeBindings(scope);
		bindings.put("a", "a");
		Object object = bindings.put("a", "b");
		assertEquals("a", object);
		assertEquals("b", scope.resolve("a"));
	}

}
