package org.dynjs.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynObjectTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void hasDefaultAttributes(){
        DynObject object = new DynObject();
        assertThat(object.getProperty("prototype")).isNotNull();
    }
}
