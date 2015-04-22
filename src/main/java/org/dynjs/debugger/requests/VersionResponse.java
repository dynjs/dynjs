package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bob McWhirter
 */
public class VersionResponse extends AbstractResponse<VersionRequest> {

    public VersionResponse(VersionRequest request, boolean success, boolean running) {
        super(request, success, running);
    }

    @JsonProperty("V8Version")
    public String getV8Version() {
        return "1.3.19 (DynJS, alike)";
    }
}
