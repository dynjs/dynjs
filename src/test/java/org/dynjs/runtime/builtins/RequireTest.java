package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;

public class RequireTest {
    private DynJS dynJS;
    private DynThreadContext context;
    private DynJSConfig config;

    @Before
    public void setUp() {
        config  = new DynJSConfig();
        config.addBuiltin("require", new Require());

        context = new DynThreadContext();
        context.addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
        dynJS   = new DynJS(config);
    }

    @Test
    public void testReturnsFalseWithoutAnArgument() {
        check("var result = !require();");
    }

    @Test
    public void testReturnsFalseWhenTheFileIsNotFound() {
        check("var result = !require('nonexistant_module');");
    }
    
    @Test
    public void testReturnsTrueWhenTheFileIsFound() {
        check("var result = require('my_module');");
    }
    
    @Test
    public void testAllowsFileExtension() {
        check("var result = require('my_module.js');");
    }
    
    @Test
    public void testExportsGlobals() {
    	check( "var result = require('my_module') && is_truth;" );
    }
    
    private void check(String scriptlet) {
        check(scriptlet, true);
    }

    private void check(String scriptlet, Boolean expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }
}
