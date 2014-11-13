package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class ClassLoaderSourceProvider implements SourceProvider {

    private final ClassLoader classLoader;
    private final String path;

    public ClassLoaderSourceProvider(ClassLoader classLoader, String path) {
        this.classLoader = classLoader;
        this.path = path;
    }

    public Reader openReader() throws IOException {
        InputStream in = this.classLoader.getResourceAsStream(this.path);
        if ( in == null ) {
            throw new FileNotFoundException( this.path );
        }

        return new InputStreamReader( in );
    }
}
