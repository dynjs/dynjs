package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSPrimitiveTest {

    @Test
    public void objectsShouldNotBePrimitives() {
        assertThat(new DynObject().isPrimitive()).isFalse();
    }


}
