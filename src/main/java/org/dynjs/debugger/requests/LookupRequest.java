package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class LookupRequest extends AbstractRequest<LookupResponse> {

    private List<Integer> handles;
    private boolean includeSource;

    public LookupRequest() {
        super("lookup");
    }

    public List<Integer> getHandles() {
        return this.handles;
    }

    public void setHandles(List<Integer> handles) {
        this.handles = handles;
    }

    public void setIncludeSource(boolean includeSource) {
        this.includeSource = includeSource;
    }

    public boolean isIncludeSource() {
        return this.includeSource;
    }


}

