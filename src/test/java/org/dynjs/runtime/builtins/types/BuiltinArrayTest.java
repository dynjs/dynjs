package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Reference;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BuiltinArrayTest extends AbstractDynJSTestSupport {

    @Test
    public void testArrayConstructorWithSize() {
        eval("var x = Array(0);");
        final Reference x = getContext().resolve("x");
        assertThat(x).isNotNull();
        assertThat(x.getValue(getContext())).isInstanceOf(DynArray.class);
        final DynArray value = (DynArray) x.getValue(getContext());
        final PropertyDescriptor descriptor = (PropertyDescriptor) value.getOwnProperty(getContext(), "length");
        assertThat(descriptor.getValue()).isEqualTo(0);
    }
}
