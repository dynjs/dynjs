package org.dynjs.jsr223;

import org.dynjs.runtime.JSObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

import javax.script.*;

import static org.fest.assertions.Assertions.*;

/**
 * @author Bob McWhirter
 */
public class ScriptEngineTest {

    private DynJSScriptEngine engine;

    @Before
    public void setUpEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = (DynJSScriptEngine) manager.getEngineByName( "dynjs" );
    }

    @Test
    public void testScriptEngineFactoryRegistration() {
        assertThat( engine ).isNotNull();
    }

    @Test
    public void testGlobalNoState() throws ScriptException {
        Object result = engine.eval( "4+2" );
        assertThat( result ).isEqualTo( 6L );
    }

    @Test
    public void testGlobalCreateState() throws ScriptException {
        engine.eval( "var foo=42");
        assertThat( engine.eval( "foo" ) ).isEqualTo( 42L );
    }

    @Test
    public void testGlobalWithGlobalState() throws ScriptException {
        engine.getBindings(ScriptContext.GLOBAL_SCOPE ).put("foo", 44L);
        assertThat(engine.eval("foo")).isEqualTo( 44L );
    }

    @Test
    public void testGlobalWithEngineState() throws ScriptException {
        engine.put( "foo", 46L );
        assertThat(engine.eval("foo")).isEqualTo( 46L );
    }

    @Test
    public void testGlobalWithEngineStateOverridingGlobalState() throws ScriptException {
        engine.getBindings(ScriptContext.GLOBAL_SCOPE ).put( "foo", 44L );
        engine.put( "foo", 46L );
        assertThat(engine.eval("foo")).isEqualTo(46L);
    }

    @Test
    public void testGlobalWithLocalStateOverridingAllOtherStateButNotModifyingIt() throws ScriptException {
        engine.getBindings(ScriptContext.GLOBAL_SCOPE ).put( "foo", 44L );
        engine.put("foo", 46L);
        engine.eval("foo=48;");
        assertThat( engine.eval( "foo" ) ).isEqualTo(48L);

        assertThat( engine.getBindings( ScriptContext.GLOBAL_SCOPE ).get( "foo" ) ).isEqualTo( 44L );
        assertThat( engine.getBindings( ScriptContext.ENGINE_SCOPE ).get( "foo" ) ).isEqualTo( 46L );
    }

    @Test
    public void testEvalOverrideEngineState() throws ScriptException {
        SimpleBindings override = new SimpleBindings();
        override.put( "foo", "tacos" );

        engine.getBindings(ScriptContext.GLOBAL_SCOPE ).put( "foo", 44L );
        engine.getBindings(ScriptContext.ENGINE_SCOPE ).put("foo", 46L);

        Object result = engine.eval( "foo", override );

        assertThat( result ).isEqualTo( "tacos" );

        assertThat( engine.getBindings( ScriptContext.GLOBAL_SCOPE ).get( "foo" ) ).isEqualTo( 44L );
        assertThat( engine.getBindings( ScriptContext.ENGINE_SCOPE ).get( "foo" ) ).isEqualTo( 46L );
    }

    @Test
    public void testArgv() throws ScriptException {
        engine.put( ScriptEngine.ARGV, new String[]{ "one", "two" });
        Object result = engine.eval( "dynjs.argv[1]" );
        assertThat( result ).isEqualTo( "two" );
    }

    @Test
    public void testInvokeMethod() throws ScriptException, NoSuchMethodException {
        Object o = engine.eval( "var o = { foo: function(arg) { return arg+42; }, bar: 84 }; o;");
        assertThat( o ).isNotNull();
        assertThat( o ).isInstanceOf( JSObject.class );

        Object result = engine.invokeMethod( o, "foo", 14 );
        assertThat( result ).isEqualTo( 56L );

        try {
            engine.invokeMethod( o, "bar" );
            fail("should have thrown ScriptException");
        } catch (ScriptException t) {
            // expected and correct
        }

        try {
            engine.invokeMethod(o, "noSuch");
            fail( "should have thrown NoSuchMethodException" );
        } catch (NoSuchMethodException e) {
            // expected and correct
        }
    }

    @Test
    public void testInvokeFunction() throws ScriptException, NoSuchMethodException {
        engine.eval( "function foo(arg) { return 42+arg; }; var bar = 99;");

        Object result = engine.invokeFunction( "foo", 12 );
        assertThat( result ).isEqualTo( 54L );

        try {
            engine.invokeFunction( "bar" );
            fail( "Should have thrown ScriptException");
        } catch (ScriptException e) {
            // expected and correct
        }

        try {
            engine.invokeFunction( "noSuch" );
            fail("should have thrown NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // expected and correct
        }
    }

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    public static interface Thingy {
        int foo();
        String bar();
    }

    @Test
    public void testGetInterface() throws ScriptException {
        engine.eval( "function foo() { return 42; }; function bar() { return 'taco'; }" );

        Thingy thingy = engine.getInterface( Thingy.class );

        assertThat( thingy.foo() ).isEqualTo( 42 );
        assertThat( thingy.bar() ).isEqualTo( "taco" );
    }

    @Test
    public void testGetInterfaceWithThis() throws ScriptException {
        Object o = engine.eval( "var o = { foo: function() { return 99; }, bar: function() { return 'bob'; } }; o;");

        assertThat( o ).isInstanceOf( JSObject.class );

        Thingy thingy = engine.getInterface( o, Thingy.class );

        assertThat( thingy.foo() ).isEqualTo( 99 );
        assertThat( thingy.bar() ).isEqualTo( "bob" );

    }
}
