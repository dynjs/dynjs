package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DynJSNamespaceTest extends AbstractDynJSTestSupport {

    @Test
    public void hasTheBuiltinDynJSObject() {
        Object dynjs = eval("dynjs");
        assertThat(dynjs).isNotNull();
    }

    @Test
    public void exposesTheRuntimeArguments() {
        Object[] arguments = {"foo", "baz"};
        getConfig().setArgv(arguments);
        assertThat(eval("dynjs.argv")).isEqualTo(arguments);
    }
    
    @Test
    public void hasGlobalCommonJSModuleVariable() {
        assertThat(eval("module").getClass()).isEqualTo(DynObject.class);
        assertThat(eval("module.id")).isEqualTo("dynjs");
    }
}
