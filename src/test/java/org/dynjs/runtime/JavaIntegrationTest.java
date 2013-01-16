package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class JavaIntegrationTest extends AbstractDynJSTestSupport {

    @Test
    public void testImplementAnInterfaceWithAccessToContext() {
        Object result = eval(
                "var x = 1;",
                "new java.lang.Runnable( {",
                "  run: function() { ",
                "    x = 42;",
                "  }",
                "} )");

        assertThat(result).isInstanceOf(Runnable.class);

        Object x = eval("x");
        assertThat(x).isEqualTo(1L);

        Runnable runnable = (Runnable) result;
        runnable.run();
        
        x = eval("x");
        assertThat(x).isEqualTo(42L);
    }
    
    @Test
    public void testImplementAnInterface() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.Foo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
        
    }
    
    @Test
    public void testImplementAnAbstractClass() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.AbstractFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
    }
    
    @Test
    public void testImplementAConcreteClass() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.DefaultFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
    }
    
    @Test
    public void testImplementAConcreteClassWithOnlyPartialOverride() {
        
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.DefaultFoo( {",
                "  getContent: function() {",
                "    return 'javascript content';",
                "  }",
                "} )");
        
        assertThat( result.doItDifferently() ).isEqualTo( "done with: javascript content" );
    }
    
    @Test
    public void testImplementAConcreteClassWithOnlyPartialOverrideInversely() {
        
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.DefaultFoo( {",
                "  doItDifferently: function() {",
                "    print('--------------');",
                "    print(this);",
                "    print(this.getContent);",
                "    return 'with javascript content: ' + this.getContent();",
                "  }",
                "} )");
        
        assertThat( result.doItDifferently() ).isEqualTo( "with javascript content: default content" );
    }
}
