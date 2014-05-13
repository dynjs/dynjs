package org.dynjs.runtime;

import org.dynjs.runtime.java.JavaMockery;
import org.junit.Test;
import org.projectodd.rephract.java.reflect.DynamicMethod;

import static org.fest.assertions.Assertions.assertThat;

public class InvokeDynamicTest extends AbstractDynJSTestSupport {

    @Test
    public void testJavaObjectsAsProperties() {
        eval("var obj = {}");
        eval("var env = java.lang.System.getenv()");
        eval("obj.path = env.get('HOME')");
        assertThat(eval("obj.path")).isEqualTo(System.getenv().get("HOME"));
    }

    @Test
    public void testObjectProperties() {
        Object result = eval(
                "var o = {",
                "  cheese: 'swiss'",
                "};",
                "o.cheese");

        assertThat(result).isEqualTo("swiss");
    }

    @Test
    public void testEnvironment() {
        Object result = eval(
                "var o = 'swiss cheese';",
                "o");

        assertThat(result).isEqualTo("swiss cheese");
    }

    @Test
    public void testFunctionExpression() {
        Object result = eval("(function(){ return 'taco'; })()");
        assertThat(result).isEqualTo("taco");
    }

    @Test
    public void testEnvironmentFunction() {
        Object result = eval("function foo(){ return 'taco'; };",
                "foo()");
        assertThat(result).isEqualTo("taco");
    }

    @Test
    public void testEnvironmentFunctionInVar() {
        Object result = eval(
                "var o = function(){ return 'swiss cheese'; }",
                "o()");

        assertThat(result).isEqualTo("swiss cheese");
    }

    @Test
    public void testObjectFunction() {
        Object result = eval(
                "var o = {",
                "  cheese: function(){ return 'swiss'; }",
                "};",
                "o.cheese()");

        assertThat(result).isEqualTo("swiss");
    }

    @Test
    public void testNew_java() {
        if (this.config.isInvokeDynamicEnabled()) {
            Object result = eval("new org.dynjs.runtime.java.JavaMockery");
            assertThat(result).isInstanceOf(JavaMockery.class);
        }
    }

    @Test
    public void testNew_java_withParams() {
        if (this.config.isInvokeDynamicEnabled()) {
            Object result = eval("new org.dynjs.runtime.java.JavaMockery(42)");
            assertThat(result).isInstanceOf(JavaMockery.class);
            assertThat(((JavaMockery) result).getValue()).isEqualTo(42L);
        }
    }

    @Test
    public void testSystemClass_becauseLanceBreaksStuff() {
        if (this.config.isInvokeDynamicEnabled()) {
            eval("var sys = java.lang.System");
            assertThat(eval("sys")).isEqualTo(java.lang.System.class);
            assertThat(eval("sys.out")).isSameAs(System.out);
            assertThat(eval("sys.out.println")).isInstanceOf(DynamicMethod.class);
            eval("sys.out.println('foo is nice')");
        }
    }

    @Test
    public void testStaticObjectAsProperty() {
        eval("var obj = {}");
        eval("obj.version = org.dynjs.runtime.DynJS.VERSION");
        assertThat(eval("obj.version")).isEqualTo(org.dynjs.runtime.DynJS.VERSION);
    }

    @Test
    public void testNew_java_withParams_getProp() {
        Object result = eval("new org.dynjs.runtime.java.JavaMockery(42).value");
        assertThat(result).isEqualTo(42L);
    }

    @Test
    public void testNew_java_withParams_getMethod_call() {
        Object result = eval("new org.dynjs.runtime.java.JavaMockery(42).doSomething()");
        assertThat(result).isEqualTo(42L);
    }

}
