package org.dynjs.runtime.modules;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.builtins.Require;
import org.dynjs.runtime.builtins.TestModuleProvider;
import org.junit.Before;
import org.junit.Test;


public class JavaClassModuleProviderTest {
    private DynJS dynJS;
    private DynThreadContext context;
    private DynJSConfig config;
    private JavaClassModuleProvider javaClassModuleProvider;
    
    @Before
    public void setUp() {
        javaClassModuleProvider = new JavaClassModuleProvider();
        config  = new DynJSConfig();
        context = new DynThreadContext();
        context.addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
        context.addModuleProvider( javaClassModuleProvider );
        dynJS   = new DynJS(config);
    }

    @Test
    public void testAnnotatedJavaModule() {
        javaClassModuleProvider.addModule( new AnnotatedJavaModule() );
        assertNotNull( javaClassModuleProvider.load( context, "tacos"  ));
        
        check( "var x = require('tacos'); var result = x.cheese();", "cheddar" );
    }
    
    private void check(String scriptlet, Object expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }
    
    @Module( name="tacos" )
    public static class AnnotatedJavaModule {
        @Export
        public String cheese(DynThreadContext context) {
            return "cheddar";
        }
    }
    
    

}
