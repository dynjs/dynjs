package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;

public class DynJSBuiltin {
    private final DynJS runtime;

    public DynJSBuiltin(DynJS runtime) {
        this.runtime = runtime;
    }

    public Object[] getArgv() {
        return this.runtime.getConfig().getArgv();
    }
}
