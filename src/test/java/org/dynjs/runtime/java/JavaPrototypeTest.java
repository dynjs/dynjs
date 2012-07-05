package org.dynjs.runtime.java;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynJSConfig;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.junit.Before;
import org.junit.Test;


public class JavaPrototypeTest {
    private DynJS dynJS;
    private DynThreadContext context;
    private DynJSConfig config;
    
    @Before
    public void setUp() {
        config  = new DynJSConfig();
        context = new DynThreadContext();
        dynJS   = new DynJS(config);
    }
    
    @Test
    public void testCreationOfDynObject() {
        DynObject dynObj = JavaPrototypeFactory.newObject( context, ProtoClass.class );
        context.getScope().define( "obj1", dynObj );
        check( "var result = obj1.saySomething();", "Something" );
    }
    
    private void check(String scriptlet, Object expected) {
        dynJS.eval(context, scriptlet);
        Object result = context.getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }

    @JavaPrototype
    public static class ProtoClass {
        @JavaPrototypeFunction
        public String saySomething() {
            return "Something";
        }
    }
    
    

}
