package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RTTest {
    @Test
    public void testAllArgsAreSameType() throws Exception {
        assertThat(RT.allArgsAreSameType(new Object[]{2L, 3L, 7L})).isTrue();
        assertThat(RT.allArgsAreSameType(new Object[]{"", 0})).isFalse();
    }
}
