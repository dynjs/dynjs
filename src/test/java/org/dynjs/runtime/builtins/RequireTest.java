package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.exception.ReferenceError;
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
        new Require();
        context = new DynThreadContext();
        context.addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
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
        check("var result = require('my_module').message;", "Hello world");
    }
    
    @Test
    public void testAllowsFileExtension() {
        check("var result = require('my_module.js').message;", "Hello world");
    }
    
    @Test
    public void testFindsPackagedModules() {
    	check("var result = require('amodule').message", "Hello world");
    }
    
    @Test
    public void testExportsFunctions() {
    	check("var result = require('my_module').sayHello();", "Hello again");
    }
    
    @Test
    public void testKeepsPrivateVariablesPrivate() {
    	try {
    		check("var result = require('my_module').privateVariable", null);
    	} catch (ReferenceError error) {
    		assertThat(error.getMessage()).isEqualTo("privateVariable not found");
    	}
    }
    
    private void check(String scriptlet, Object expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }
}
