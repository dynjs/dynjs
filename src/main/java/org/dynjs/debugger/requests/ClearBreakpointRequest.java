package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class ClearBreakpointRequest extends AbstractRequest<ClearBreakpointResponse> {

    private long breakpoint;

    public ClearBreakpointRequest() {
        super("clearbreakpoint");
    }


    public void setBreakpoint(long breakpoint) {
        this.breakpoint = breakpoint;
    }

    public long getBreakpoint() {
        return this.breakpoint;
    }


}

