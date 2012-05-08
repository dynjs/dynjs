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
        check("var result = (null == require());");
    }

    @Test
    public void testReturnsNullWhenTheFileIsNotFound() {
        check("var result = (null == require('nonexistant_module'));");
    }
    
    @Test
    public void testReturnsTrueWhenTheFileIsFound() {
        check("var result = require('my_module');");
    }
    
    @Test
    public void testAllowsFileExtension() {
        check("var result = require('my_module.js');");
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
