package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dynjs.runtime.SourceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Bob McWhirter
 */
public class ScriptsResponse extends AbstractResponse<ScriptsRequest> {

    private final Set<SourceProvider> scripts = new HashSet<>();
    private final boolean includeSource;

    public ScriptsResponse(ScriptsRequest request, Set<SourceProvider> scripts, boolean includeSource, boolean success, boolean running) {
        super(request, success, running);
        this.scripts.addAll( scripts );
        this.includeSource = includeSource;
    }

    @JsonIgnore
    public Collection<SourceProvider> getScripts() {
        return this.scripts;
    }

    @JsonIgnore
    public boolean isIncludeSource() {
        return this.includeSource;
    }


}
