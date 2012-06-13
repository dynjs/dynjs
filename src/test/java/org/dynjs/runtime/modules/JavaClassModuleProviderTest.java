package org.dynjs.runtime.modules;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynThreadContext;
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
        javaClassModuleProvider.addModule( new AnnotatedJavaModule() );
        config  = new DynJSConfig();
        context = new DynThreadContext();
        context.addLoadPath(System.getProperty("user.dir") + "/src/test/resources/org/dynjs/runtime/builtins/");
        context.addModuleProvider( javaClassModuleProvider );
        dynJS   = new DynJS(config);
    }

    @Test
    public void testMethodWithOnlySelfAndContext() {
        check( "var x = require('tacos'); var result = x.cheese();", "cheddar" );
    }
    
    @Test
    public void testMethodWithNoArguments() {
        check( "var x = require('tacos'); var result = x.lunch();", "beef" );
    }
    
    @Test
    public void testMethodWithNoDynJSArguments() {
        check( "var x = require('tacos'); var result = x.breakfast('bacon');", "eggs and bacon" );
    }
    
    @Test
    public void testMethodWithDynJSArgumentsAndMore() {
        check( "var x = require('tacos'); var result = x.dinner('soup');", "fish soup" );
    }
    private void check(String scriptlet, Object expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }
    
    @Module( name="tacos" )
    public static class AnnotatedJavaModule {
        @Export
        public String cheese(Object self, DynThreadContext context) {
            return "cheddar";
        }
        
        @Export
        public String lunch() {
            return "beef";
        }
        
        @Export
        public String breakfast(String side) {
            return "eggs and " + side;
        }
        
        @Export
        public String dinner(Object self, DynThreadContext context, String style) {
            return "fish " + style;
        }
    }
    
    

}
