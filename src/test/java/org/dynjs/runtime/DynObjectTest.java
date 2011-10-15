package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveBoolean;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynObjectTest {

    private DynObject object;

    @Before
    public void setUp() throws Exception {
        object = new DynObject();
    }

    @Test
    public void hasDefaultAttributes() {
        assertThat(object.getProperty("prototype")).isNotNull();
    }

    @Test
    public void aDefinedObjectExists() {
        object.define("meh", new DynObject());
        assertThat(object.resolve("meh")).isNotNull();
    }

    @Test
    public void testEquality() {
        DynPrimitiveNumber n1 = new DynPrimitiveNumber("8", 10);
        DynPrimitiveNumber n2 = new DynPrimitiveNumber("8", 10);
        assertThat(DynObject.eq(n1, n2)).isEqualTo(true);
    }

}
