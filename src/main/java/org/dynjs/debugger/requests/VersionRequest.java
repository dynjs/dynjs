package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class VersionRequest extends AbstractRequest<VersionResponse> {

    public VersionRequest() {
        super("version");
    }

}

