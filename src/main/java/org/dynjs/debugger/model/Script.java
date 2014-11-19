package org.dynjs.debugger.model;

import org.dynjs.runtime.SourceProvider;

/**
 * @author Bob McWhirter
 */
public class Script {

    private final SourceProvider source;

    public Script(SourceProvider source) {
        this.source = source;
    }
}
