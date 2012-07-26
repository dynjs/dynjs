package org.dynjs.runtime;

import org.junit.Before;

import static org.fest.assertions.Assertions.assertThat;

public abstract class AbstractDynJSTestSupport {
    protected DynJS dynJS;
    protected DynThreadContext context;
    protected DynJSConfig config;

    @Before
    public void setUp() {
        config = new DynJSConfig();
        dynJS = new DynJS(getConfig());
        context = new DynThreadContext();
    }

    protected void check(String scriptlet) {
        check(scriptlet, true);
    }

    protected void check(String scriptlet, Boolean expected) {
        getDynJS().eval(getContext(), scriptlet);
        Object result = getContext().getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }

    protected void check(String scriptlet, Object expected) {
        getDynJS().eval(getContext(), scriptlet);
        Object result = getContext().getScope().resolve("result");
        assertThat(result).isEqualTo(expected);
    }

    public DynJS getDynJS() {
        return dynJS;
    }

    public DynThreadContext getContext() {
        return context;
    }

    public DynJSConfig getConfig() {
        return config;
    }

    protected Object resultFor(String expression) {
        getDynJS().eval(getContext(), expression);
        return getContext().getScope().resolve("result");
    }
}
