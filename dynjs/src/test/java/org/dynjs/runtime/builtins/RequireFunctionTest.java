package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;

public class RequireFunctionTest {
    private DynJS dynJS;
    private DynThreadContext context;
    private DynJSConfig config;

    @Before
    public void setUp() {
        config  = new DynJSConfig();
        dynJS   = new DynJS(config);
        context = new DynThreadContext();
        config.addBuiltin("require", new RequireFunction());
    }

    @Test
    public void testReturnsFalseWithoutAnArgument() {
        check("var result = !require();");
    }

    @Test
    public void testReturnsFalseWhenTheFileIsNotFound() {
        check("var result = !require('my_module');");
    }
    
    @Test
    public void testReturnsTrueWhenTheFileIsFound() {
        check("var result = require('./src/test/resources/org/dynjs/runtime/builtins/require_my_module.js');");
    }
    
    @Test
    public void testExportsGlobals() {
    	check( "var result = require('./src/test/resources/org/dynjs/runtime/builtins/require_my_module.js') && is_truth;" );
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
