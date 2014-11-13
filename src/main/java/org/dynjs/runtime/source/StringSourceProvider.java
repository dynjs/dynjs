package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class StringSourceProvider implements SourceProvider {

    private final String source;

    public StringSourceProvider(String source) {
        this.source = source;
    }

    public Reader openReader() throws IOException {
        return new StringReader( this.source );
    }

    public String toString() {
        return "[StringSourceProvider: " + source.substring(0, 200) + "...]";

    }
}
