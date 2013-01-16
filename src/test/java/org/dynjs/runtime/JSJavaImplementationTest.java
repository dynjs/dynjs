package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import java.lang.reflect.Constructor;

import org.dynjs.runtime.linker.java.JSJavaImplementationManager;
import org.junit.Test;

public class JSJavaImplementationTest extends AbstractDynJSTestSupport {

    
    @Test
    public void testInterfaceImplementation() throws Exception {
        
        JSJavaImplementationManager manager = new JSJavaImplementationManager();
        
        Class<Runnable> implClass  = (Class<Runnable>) manager.getImplementationWrapper(Runnable.class);
        
        assertThat( implClass ).isNotNull();
        
        JSObject impl = (JSObject) eval( "var o = { run: function(){ print('howdy'); } }; o" );
        
        Constructor<Runnable> ctor = implClass.getConstructor(ExecutionContext.class, JSObject.class);
        
        Runnable runnable = ctor.newInstance(getContext(), impl);
        
        runnable.run();
        
        
        
    }
}