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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.dynjs.api.Scope;
import org.junit.Test;

public class ScopeBindingsTest {

	@Test
	public void should_get_object_put() {
		ScopeBindings bindings = new ScopeBindings();
		Scope scope = bindings.asScope();
		Object object = bindings.put("a", "a");
		assertNull(object);
		assertEquals("a", scope.resolve("a"));
		assertEquals("a", bindings.get("a"));
	}

	@Test
	public void should_return_previously_put() {
		ScopeBindings bindings = new ScopeBindings();
		Scope scope = bindings.asScope();
		bindings.put("a", "a");
		Object object = bindings.put("a", "b");
		assertEquals("a", object);
		assertEquals("b", scope.resolve("a"));
		assertEquals("b", bindings.get("a"));
	}

}
