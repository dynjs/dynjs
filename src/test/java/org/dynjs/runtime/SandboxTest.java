package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class SandboxTest extends AbstractDynJSTestSupport {
    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessDynJSGlobalWhenRunningInSandbox() {
        eval("var foo = dynjs;");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessJavaPackageWhenRunningInSandbox() {
        eval("var foo = java;");
    }

    @Test(expected = ThrowException.class)
    public void jsCodeCannotAccessOrgPackageWhenRunningInSandbox() {
        eval("var foo = org;");
    }

    @Override
    protected org.dynjs.Config createConfig() {
        Config config = super.createConfig();
        config.setSandbox(true);
        return config;
    }
}
