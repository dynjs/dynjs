package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynamicClassLoader;

import java.net.MalformedURLException;

public class DynJSBuiltin {
    private final DynJS runtime;
    private final Classpath classpath;

    public DynJSBuiltin(DynJS runtime) {
        this.runtime = runtime;
        classpath = new Classpath(runtime.getConfig().getClassLoader());
    }

    public Object[] getArgv() {
        return this.runtime.getConfig().getArgv();
    }

    public Classpath getClasspath() {
        return classpath;
    }

    public static class Classpath {
        private final DynamicClassLoader classLoader;

        private Classpath(DynamicClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public void push(String entry) throws MalformedURLException {
            this.classLoader.append(entry);
        }
    }

}
