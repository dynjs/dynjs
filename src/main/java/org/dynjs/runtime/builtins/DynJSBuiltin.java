package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynamicClassLoader;

import java.net.MalformedURLException;
import java.net.URL;

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

        @Override
        public String toString() {
            if (classLoader.getURLs().length == 0) { return "[]"; }
            StringBuffer stringBuffer = new StringBuffer("[");
            for (URL url : classLoader.getURLs()) {
                stringBuffer.append(url.toExternalForm());
                stringBuffer.append(", ");
            }
            // chop off the last two characters: ", "
            stringBuffer.delete(stringBuffer.length()-2, stringBuffer.length());
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
    }

}
