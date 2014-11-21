package org.dynjs.runtime.source;

import org.dynjs.runtime.SourceProvider;

import java.io.*;

/**
 * @author Bob McWhirter
 */
public class StringSourceProvider implements SourceProvider {

    private static int COUNTER = 0;

    private final String source;
    private String name;
    private final int id;

    public StringSourceProvider(String source) {
        this( source, "<eval>" );
    }

    public StringSourceProvider(String source, String name) {
        this.source = source;
        this.name = name;
        this.id = ++COUNTER;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    public long getLineCount() {
        return 99;
    }

    public long getSourceLength() {
        return 99;
    }

    public Reader openReader() throws IOException {
        return new StringReader( this.source );
    }

    public String toString() {
        return "[StringSourceProvider: " + source.substring(0, 200) + "...]";

    }
}
