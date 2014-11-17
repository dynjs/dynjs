package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.debugger.agent.handlers.WrappingHandler;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ResponseWrapper {

    private final Response body;
    private final int seq;

    public ResponseWrapper(Response body) {
        this.body = body;
        this.seq = WrappingHandler.seqCounter.incrementAndGet();
    }

    public String getType() {
        return "response";
    }

    public String getCommand() {
        return this.body.getCommand();
    }

    public Response getBody() {
        return this.body;
    }

    public boolean isRunning() {
        return this.body.isRunning();
    }

    public boolean isSuccess() {
        return this.body.isSuccess();
    }

    public int getSeq() {
        return this.seq;
    }

    public List<Object> getRefs() {
        return this.body.getRefs();
    }

    @JsonProperty("request_seq")
    public int getRequestSeq() {
        return this.body.getRequest().getSeq();

    }
}
