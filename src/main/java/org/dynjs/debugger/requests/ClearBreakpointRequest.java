package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ClearBreakpointRequest extends AbstractRequest<ClearBreakpointResponse> {

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

