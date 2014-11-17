package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class FileSourceProvider extends InputStreamSourceProvider {

    public FileSourceProvider(File file) throws IOException {
        super( new FileInputStream( file ), file.getAbsolutePath() );
    }

}
