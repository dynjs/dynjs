package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSNamespaceTest extends AbstractDynJSTestSupport {

    @Test
    public void hasTheBuiltinDynJSObject() {
        Object dynjs = eval("dynjs");
        assertThat(dynjs).isNotNull();
    }
}
