package org.dynjs.debugger.model;

import org.dynjs.runtime.SourceProvider;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class Script {

    private final SourceProvider source;
    private final boolean includeSource;

    public Script(SourceProvider source, boolean includeSource) {
        this.source = source;
        this.includeSource = includeSource;
    }

    public boolean isIncludeSource() {
        return this.includeSource;
    }

    public int getId() {
        return this.source.getId();
    }

    public String getName() {
        return this.source.getName();
    }

    public String getSource() throws IOException {
        return this.source.getSource();
    }

    public long getSourceLength() {
        return this.source.getSourceLength();
    }

    public long getLineCount() {
        return this.source.getLineCount();
    }



}
