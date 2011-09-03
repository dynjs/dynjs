package org.dynjs.runtime;

import org.dynjs.exception.ReferenceError;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynObjectTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void hasDefaultAttributes() {
        DynObject object = new DynObject();
        assertThat(object.getProperty("prototype")).isNotNull();
    }

    @Test(expected = ReferenceError.class)
    public void throwsReferenceErrorOnMissingReference() {
        DynObject object = new DynObject();
        object.resolve("inexistentAttribute");
    }
}
