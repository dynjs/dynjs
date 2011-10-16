package org.dynjs.runtime;

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

}
