package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynJS;

import java.net.MalformedURLException;

public class DynJSBuiltin {
    private final DynJS runtime;

    public DynJSBuiltin(DynJS runtime) {
        this.runtime = runtime;
    }

    public Object[] getArgv() {
        return this.runtime.getConfig().getArgv();
    }

    public void addClassPathEntry(String path) throws MalformedURLException {
        this.runtime.getConfig().getClassLoader().append(path);
    }
}
