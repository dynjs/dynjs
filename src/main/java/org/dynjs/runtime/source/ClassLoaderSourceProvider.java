package org.dynjs.runtime.source;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class ClassLoaderSourceProvider extends InputStreamSourceProvider {

    public ClassLoaderSourceProvider(ClassLoader classLoader, String path) throws IOException {
        super(classLoader.getResourceAsStream(path), path);
    }
}
