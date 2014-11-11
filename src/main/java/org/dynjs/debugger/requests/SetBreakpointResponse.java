package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bob McWhirter
 */
public class SetBreakpointResponse extends AbstractResponse<SetBreakpointRequest> {

    private final long num;

    public SetBreakpointResponse(SetBreakpointRequest request, long num, boolean success, boolean running) {
        super(request, success, running);
        this.num = num;
    }

    @JsonProperty("breakpoint")
    public long getNumber() {
        return this.num;
    }

    public String getType() {
        return getRequest().getArguments().getType();
    }
}
