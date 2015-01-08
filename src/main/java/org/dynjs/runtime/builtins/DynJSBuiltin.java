package org.dynjs.runtime.builtins;

import org.dynjs.runtime.Classpath;
import org.dynjs.runtime.DynJS;

public class DynJSBuiltin {
    private final DynJS runtime;

    public DynJSBuiltin(DynJS runtime) {
        this.runtime = runtime;
    }

    public Object[] getArgv() {
        return this.runtime.getConfig().getArgv();
    }

    public Classpath getClasspath() {
        return this.runtime.getConfig().getClasspath();
    }

    public DynJS getRuntime() {
        return this.runtime;
    }

}
