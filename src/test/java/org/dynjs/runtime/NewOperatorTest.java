package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.java.JavaMockery;
import org.dynjs.runtime.linker.java.MethodMeta;
import org.junit.Test;

public class NewOperatorTest extends AbstractDynJSTestSupport {

    @Test(expected=ThrowException.class)
    public void testInvalidNew() {
        eval( "new new Boolean(true)" );
    }
    
    @Test
    public void testNew() {
        eval("var y;",
                "function Thing(){ y = this };",
                " var z = { ctor: Thing };",
                "var x = new z.ctor(42);");

        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        JSFunction thing = (JSFunction) getContext().resolve("Thing").getValue(getContext());

        JSObject y = (JSObject) getContext().resolve("y").getValue(getContext());

        assertThat(y).isSameAs(x);
    }

    @Test
    public void testNewWithoutParens() {
        eval("var y;",
                "function Thing(){ y = this };",
                "var x = new Thing;");

        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        JSFunction thing = (JSFunction) getContext().resolve("Thing").getValue(getContext());

        JSObject y = (JSObject) getContext().resolve("y").getValue(getContext());

        assertThat(y).isSameAs(x);
    }

    @Test
    public void testNewSettingThisProps() {
        eval("function Thing(){ this.taco='fish'; };",
                "var x = new Thing();");

        JSObject x = (JSObject) getContext().resolve("x").getValue(getContext());
        Object result = x.get(getContext(), "taco");
        assertThat(result).isEqualTo("fish");
    }
    
    @Test
    public void testNew_java() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery");
        assertThat( result ).isInstanceOf(JavaMockery.class);
    }
    
    @Test
    public void testNew_java_withParams() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42)");
        assertThat( result ).isInstanceOf(JavaMockery.class);
        assertThat( ((JavaMockery)result).getValue() ).isEqualTo( 42L );
    }
    
    @Test
    public void testNew_java_withParams_getProp() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).value");
        assertThat( result ).isEqualTo( 42L );
    }
    
    @Test
    public void testNew_java_withParams_getMethod() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething");
        assertThat( result ).isInstanceOf(MethodMeta.class);
        assertThat( ((MethodMeta)result).getName() ).isEqualTo( "doSomething" );
    }
    
    @Test
    public void testNew_java_withParams_getMethod_call() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething()");
        assertThat( result ).isEqualTo( 42L );
    }
    

}
