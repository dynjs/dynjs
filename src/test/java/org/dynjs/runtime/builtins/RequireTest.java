package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test basic require() functionality.
 */
public class RequireTest extends AbstractDynJSTestSupport {

    @Test
    public void testRequirePaths() {
        assertThat(eval("require.paths")).isInstanceOf(ArrayList.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddAndRemoveLoadPath() {
        eval("require.addLoadPath('/foo')");
        List<String> paths = (List<String>) eval("require.paths");
        assertThat(paths.contains("/foo"));
        eval("require.removeLoadPath('/foo')");
        assertThat(!paths.contains("/foo"));
    }

}
