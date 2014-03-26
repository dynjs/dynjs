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
