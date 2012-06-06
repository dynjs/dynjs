/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DynThreadContextTest {
    private DynThreadContext context;

    @Before
    public void setUp() {
        context = new DynThreadContext();
    }

	@Test
	public void testLoadPathExists() {
        assertThat(context.getLoadPaths()).isNotNull();
	}

	@Test
	public void testLoadPathContainsDefaults() {
        // TODO: This should include the classpath (perhaps), and the expected
        // load paths for Node.js parity. DYNJS-42
        List<String> paths = context.getLoadPaths();
        assertThat(paths.size()).isGreaterThan(0);
        assertThat(paths.contains(System.getProperty("user.dir") + "/")).isTrue();
        assertThat(paths.contains(System.getProperty("user.home") + "/.node_modules/")).isTrue();
        assertThat(paths.contains(System.getProperty("user.home") + "/.node_libraries/")).isTrue();
        assertThat(paths.contains("/usr/local/lib/node/")).isTrue();
	}
	
	@Test
	public void testLoadPathsCanBeAppendedTo() {
		int originalSize = context.getLoadPaths().size();
		context.addLoadPath("/path/to/my/modules");
		List<String> paths = context.getLoadPaths();
		assertThat(originalSize + 1).isEqualTo(paths.size());
	}
	
	@Test
	public void testLoadPathsCanBeCompletelyReplaced() {
		List<String> originalLoadPaths = context.getLoadPaths();
		List<String> newLoadPaths = new ArrayList<String>();
		newLoadPaths.add("/path/one");
		newLoadPaths.add("/path/two");
		context.setLoadPaths(newLoadPaths);
		List<String> paths = context.getLoadPaths();
		assertThat(paths.size()).isEqualTo(newLoadPaths.size());
		assertThat(paths.get(0)).isEqualTo(newLoadPaths.get(0));
		assertThat(paths.get(1)).isEqualTo(newLoadPaths.get(1));
		assertThat(paths).isNotSameAs(originalLoadPaths);
		assertThat(paths).isNotSameAs(newLoadPaths);
	}
}
