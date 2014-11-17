package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
public class ClearBreakpointRequest extends AbstractRequest<ClearBreakpointResponse> {

    @JsonIgnoreProperties("maxStringLength")
    public static class Arguments {
        private long breakpoint;

        public void setBreakpoint(long breakpoint) {
            this.breakpoint = breakpoint;
        }

        public long getBreakpoint() {
            return this.breakpoint;
        }

    }

    private Arguments arguments;

    public ClearBreakpointRequest() {
        super("clearbreakpoint");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

}

