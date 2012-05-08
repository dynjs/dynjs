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
