package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.debugger.events.Event;

/**
 * @author Bob McWhirter
 */
public class ResponseWrapper {

    private final Response body;

    public ResponseWrapper(Response body) {
        this.body = body;
    }

    public String getCommand() {
        return this.body.getCommand();
    }

    public Response getBody() {
        return this.body;
    }

    @JsonProperty("request_seq")
    public int getRequestSeq() {
        return this.body.getRequest().getSeq();

    }
}
