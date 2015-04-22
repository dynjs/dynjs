package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class ReaderSourceProvider extends StringSourceProvider {

    public ReaderSourceProvider(Reader source) throws IOException {
        this(source, "<script>");
    }

    public ReaderSourceProvider(Reader source, String name) throws IOException {
        super( readFully( source ), name );
    }

    private static String readFully(Reader source) throws IOException {
        StringBuilder builder = new StringBuilder();

        char[] buf = new char[1024];
        int numRead = -1;

        while ( ( numRead = source.read(buf) ) >= 0 ) {
            if ( numRead > 0 ) {
                builder.append( buf, 0, numRead );
            }
        }

        source.close();

        return builder.toString();
    }

}
