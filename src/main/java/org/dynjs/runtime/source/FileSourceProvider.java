package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class FileSourceProvider implements SourceProvider {

    private final File file;

    public FileSourceProvider(File file) {
        this.file = file;
    }

    public Reader openReader() throws IOException {
        return new FileReader( this.file );
    }
}
