package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dynjs.debugger.model.Script;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bob McWhirter
 */
public class ScriptsResponse extends AbstractResponse<ScriptsRequest> implements ListResponse {

    private final Set<Script> scripts = new HashSet<>();
    private final boolean includeSource;

    public ScriptsResponse(ScriptsRequest request, Set<Script> scripts, boolean includeSource, boolean success, boolean running) {
        super(request, success, running);
        this.scripts.addAll( scripts );
        this.includeSource = includeSource;
    }

    public Collection<Script> getScripts() {
        return this.scripts;
    }

    @Override
    public Collection<?> getValues() {
        return getScripts();
    }

    @JsonIgnore
    public boolean isIncludeSource() {
        return this.includeSource;
    }


}
