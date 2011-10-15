package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynPropertyTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void allowsAttributeChaining() {
        DynProperty property = new DynProperty("x")
                .setAttribute("value", DynThreadContext.UNDEFINED)
                .setAttribute("writable", true)
                .setAttribute("enumerable", true)
                .setAttribute("configurable", true);
        assertThat(property.getAttribute("value")).isEqualTo(DynThreadContext.UNDEFINED);
        assertThat(property.getAttribute("writable")).isEqualTo(true);
        assertThat(property.getAttribute("enumerable")).isEqualTo(true);
        assertThat(property.getAttribute("configurable")).isEqualTo(true);
    }

}
