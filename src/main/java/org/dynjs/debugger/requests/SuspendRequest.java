package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class SuspendRequest extends AbstractRequest<SuspendResponse> {

    public SuspendRequest() {
        super("suspend");
    }

}

