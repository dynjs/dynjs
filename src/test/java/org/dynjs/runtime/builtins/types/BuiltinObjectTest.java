package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.junit.Test;

public class BuiltinObjectTest extends AbstractDynJSTestSupport {

    @Test
    public void testConstructorWithObject() {
        eval("var x = {}; var y = new Object(x);");

        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());

        assertThat(y).isSameAs(x);
    }

    @Test
    public void testConstructorWithString() {
        eval("var x = new Object( 'howdy' );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo("howdy");
    }

    @Test
    public void testConstructorWithBoolean() {
        eval("var x = new Object( true );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo(true);
    }

    @Test
    public void testConstructorWithInteger() {
        eval("var x = new Object( 42 );");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(PrimitiveDynObject.class);

        PrimitiveDynObject primitiveX = (PrimitiveDynObject) x;
        assertThat(primitiveX.getPrimitiveValue()).isEqualTo(42);
    }

    @Test
    public void testConstructWithoutArg() {
        eval("var x = new Object();");
        Object x = getContext().resolve("x").getValue(getContext());
        assertThat(x).isInstanceOf(DynObject.class);
    }

    @Test
    public void testFreeze() {
        Object result = eval("var x = { foo: 'taco' };",
                "x.foo = 'cheese';",
                "x.foo;");

        assertThat(result).isEqualTo("cheese");
        
        assertThat( eval( "Object.isFrozen(x)")).isEqualTo(false);

        result = eval("Object.freeze(x);",
                "x.foo = 'fish';",
                "x.foo;");
        assertThat( eval( "Object.isFrozen(x)")).isEqualTo(true);

        assertThat(result).isEqualTo("cheese");
    }

    @Test
    public void testGetPrototypeOf() {
        assertThat(eval("var x = Function(); Object.getPrototypeOf(x)")).isSameAs(eval("Function.prototype"));
    }

    @Test
    public void testGetOwnPropertyDescriptor_Data() {
        JSObject result = (JSObject) eval("var x = { foo: 'taco' };",
                "Object.getOwnPropertyDescriptor(x, 'foo');");

        assertThat(result).isNotNull();

        assertThat(result.get(getContext(), "value")).isEqualTo("taco");
        assertThat(result.get(getContext(), "writable")).isEqualTo(true);
        assertThat(result.get(getContext(), "configurable")).isEqualTo(true);
        assertThat(result.get(getContext(), "enumerable")).isEqualTo(true);
    }

    @Test
    public void testGetOwnPropertyDescriptor_Accessor() {
        JSObject result = (JSObject) eval("var x = { set foo(x){ this.internal_foo = x}, get foo(){ this.internal_foo } }",
                "Object.getOwnPropertyDescriptor(x, 'foo');");

        assertThat(result).isNotNull();

        assertThat(result.get(getContext(), "get")).isNotNull().isInstanceOf(JSFunction.class);
        assertThat(result.get(getContext(), "set")).isNotNull().isInstanceOf(JSFunction.class);
        assertThat(result.get(getContext(), "configurable")).isEqualTo(true);
        assertThat(result.get(getContext(), "enumerable")).isEqualTo(true);

    }

    @Test
    public void testGetOwnPropertyNames() {
        JSObject result = (JSObject) eval("var x = {",
                "foo: 42,",
                "set bar(x){ this.internal_bar=x },",
                "get bar(){ return this.internal_bar},",
                "}",
                "Object.getOwnPropertyNames(x)");

        assertThat(result).isNotNull();
        assertThat(result.get(getContext(), "length")).isEqualTo(2);

        List<String> names = new ArrayList<>();
        names.add((String) result.get(getContext(), "0"));
        names.add((String) result.get(getContext(), "1"));

        assertThat(names).contains("foo");
        assertThat(names).contains("bar");
    }

    @Test
    public void testDefineProperty() {
        Object result = eval("var x = {};",
                "Object.defineProperty( x, 'foo', { value: 'toast' });",
                "x.foo");

        assertThat(result).isEqualTo("toast");
    }

    @Test
    public void testDefineProperties() {
        eval("var attrs = {",
                " foo: { value: 'toast' },",
                " fish: { value: 'taco' },",
                "};",
                "var x = {};",
                "Object.defineProperties(x, attrs);");

        assertThat(eval("x.foo")).isEqualTo("toast");
        assertThat(eval("x.fish")).isEqualTo("taco");

    }

    @Test
    public void testCreateWithoutProps() {
        eval("var x = { taco: 'fish' };",
                "var y = Object.create(x);");

        assertThat(eval("Object.getPrototypeOf(y)")).isSameAs(eval("x"));
        assertThat(eval("y.taco")).isEqualTo("fish");
    }

    @Test
    public void testCreateWithProps() {
        eval("var x = { taco: 'fish' };",
                "var y = Object.create(x, { cheese: { value: 'cheddar' } });");

        assertThat(eval("Object.getPrototypeOf(y)")).isSameAs(eval("x"));
        assertThat(eval("y.taco")).isEqualTo("fish");
        assertThat(eval("y.cheese")).isEqualTo("cheddar");
    }

    @Test
    public void testSeal() {
        eval("var x = { taco: 'fish' };");
        assertThat(eval("Object.isSealed(x)")).isEqualTo(false);
        eval( "Object.seal( x )" );
        assertThat(eval("Object.isSealed(x)")).isEqualTo(true);
    }
    
    @Test
    public void testExtensibility() {
        eval( "var x = { taco: 'fish' }");
        assertThat( eval("Object.isExtensible(x)" ) ).isEqualTo(true);
        eval( "Object.preventExtensions(x)");
        assertThat( eval("Object.isExtensible(x)" ) ).isEqualTo(false);
    }
    
    @Test
    public void testKeys() {
        JSObject result = (JSObject) eval("var x = {",
                "foo: 42,",
                "set bar(x){ this.internal_bar=x },",
                "get bar(){ return this.internal_bar},",
                "}",
                "Object.keys(x)");

        assertThat(result).isNotNull();
        assertThat(result.get(getContext(), "length")).isEqualTo(2);

        List<String> names = new ArrayList<>();
        names.add((String) result.get(getContext(), "0"));
        names.add((String) result.get(getContext(), "1"));

        assertThat(names).contains("foo");
        assertThat(names).contains("bar");
    }
}
