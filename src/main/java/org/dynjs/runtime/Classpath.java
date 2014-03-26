package org.dynjs.runtime;

import java.net.MalformedURLException;
import java.net.URL;

public class Classpath {
    private final DynamicClassLoader classLoader;

    public Classpath(DynamicClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void push(String entry) throws MalformedURLException {
        this.classLoader.append(entry);
    }

    @Override
    public String toString() {
        if (classLoader.getURLs().length == 0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (URL url : classLoader.getURLs()) {
            builder.append(url.toExternalForm());
            builder.append(", ");
        }
        // chop off the last two characters: ", "
        builder.delete(builder.length() - 2, builder.length());
        builder.append("]");
        return builder.toString();
    }
}
