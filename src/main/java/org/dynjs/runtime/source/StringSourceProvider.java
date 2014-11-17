package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class StringSourceProvider implements SourceProvider {

    private final String source;
    private final String name;

    public StringSourceProvider(String source) {
        this( source, "<script>" );
    }

    public StringSourceProvider(String source, String name) {
        this.source = source;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Reader openReader() throws IOException {
        return new StringReader( this.source );
    }

    public String toString() {
        return "[StringSourceProvider: " + source.substring(0, 200) + "...]";

    }
}
