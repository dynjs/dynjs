package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class SandboxTest extends AbstractDynJSTestSupport {
    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessDynJSGlobalWhenRunningInSandbox() {
        eval("dynjs");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessJavaPackageWhenRunningInSandbox() {
        eval("java");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessOrgPackageWhenRunningInSandbox() {
        eval("org");
    }

    @Override
    protected org.dynjs.Config createConfig() {
        Config config = super.createConfig();
        config.setSandbox(true);
        return config;
    }
}
