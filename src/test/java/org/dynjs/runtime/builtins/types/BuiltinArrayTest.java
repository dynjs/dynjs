package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Reference;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BuiltinArrayTest extends AbstractDynJSTestSupport {

    @Test
    public void testArrayConstructorWithSizeZero() {
        eval("var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get( getContext(), "length") ).isEqualTo(0);
    }
    
    @Test
    public void testArrayConstructorWithSizeNonZero() {
        eval("var x = new Array(42);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get( getContext(), "length") ).isEqualTo(42);
    }
    
    @Test
    public void testArrayConstructorWithSizeNonNumeric() {
        eval("var x = new Array('taco');");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get( getContext(), "length") ).isEqualTo(1);
        assertThat(x.get( getContext(), "0") ).isEqualTo("taco");
    }

    @Test
    public void testArrayConstructorWithElementList() {
        eval("var x = new Array(1,'lol', 2);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat(x.get( getContext(), "length") ).isEqualTo(3);
        assertThat(x.get( getContext(), "0") ).isEqualTo(1);
        assertThat(x.get( getContext(), "1") ).isEqualTo( "lol");
        assertThat(x.get( getContext(), "2") ).isEqualTo(2);
    }
    
    @Test
    public void testArrayPrototype() {
        eval( "var x = new Array(0);");
        DynArray x = (DynArray) getContext().resolve("x").getValue(getContext());
        assertThat( x.getPrototype() ).isInstanceOf( DynArray.class );
    }
    
}
