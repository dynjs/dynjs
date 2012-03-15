package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.dynjs.runtime.DynThreadContext.UNDEFINED;
import static org.fest.assertions.Assertions.assertThat;

public class DynArrayTest {

    private DynArray array;

    @Before
    public void setUp() throws Exception {
        array = new DynArray();
    }

    @Test
    public void undefinedByDefault() {
        assertThat(array.get(11)).isEqualTo(UNDEFINED);
        assertThat(array.get(550)).isEqualTo(UNDEFINED);
    }

    @Test
    public void storesAndRetrievesValues() {
        Object o1 = new Object();
        array.set(10, o1);
        assertThat(array.get(10)).isEqualTo(o1);
    }

    @Test
    public void growsAsNeeded() {
        final Object o1 = new Object();
        array.set(17, o1);
        assertThat(array.get(17)).isEqualTo(o1);
        assertThat(array.length()).isEqualTo(18);
    }
}
