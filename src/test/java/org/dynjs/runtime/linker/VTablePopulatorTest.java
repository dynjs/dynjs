package org.dynjs.runtime.linker;

import org.dynjs.runtime.linker.anno.CompanionFor;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class VTablePopulatorTest {

    @Test
    public void testVtableFrom() throws Exception {
        Map<String, MethodHandle> vtable = VTablePopulator.vtableFrom(VTableTest.class);
        Assertions.assertThat(vtable.keySet().size())
                .isEqualTo(1);
    }

    @CompanionFor
    public static class VTableTest {

        public void add(Double a, Double b) {

        }
    }
}
