package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class SandboxTest extends AbstractDynJSTestSupport {
    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessDynJSGlobalWhenSandboxed() {
        eval("dynjs");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessJavaPackageWhenSandboxed() {
        eval("java");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessOrgPackageWhenSandboxed() {
        eval("org");
    }

    @Override
    protected org.dynjs.Config createConfig() {
        Config config = super.createConfig();
        config.setSandboxed(true);
        return config;
    }
}
