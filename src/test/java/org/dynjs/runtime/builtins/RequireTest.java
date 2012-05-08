package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;

public class RequireTest {
    private DynJS dynJS;
    private DynThreadContext context;
    private DynJSConfig config;
    private Require require;

    @Before
    public void setUp() {
        config  = new DynJSConfig();
        require = new Require();
        context = new DynThreadContext();
        context.addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
        config.addBuiltin("require", DynJSCompiler.wrapFunction(context, require));
        dynJS   = new DynJS(config);
    }

    @Test
    public void testReturnsNullWithoutAnArgument() {
        check("var result = require();", null);
    }

    @Test
    public void testReturnsNullWhenTheFileIsNotFound() {
        check("var result = require('nonexistant_module');", null);
    }
    
    @Test
    public void testReturnsExportsWhenTheFileIsFound() {
        check("var result = require('my_module');", "Hello world");
    }
    
    @Test
    public void testAllowsFileExtension() {
        check("var result = require('my_module.js');", "Hello world");
    }
    
    private void check(String scriptlet, Object expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        System.err.println("EXPECTING: " + expected);
        System.err.println("RESULT IS: " + result);
        assertThat(result).isEqualTo(expected);
    }
}
