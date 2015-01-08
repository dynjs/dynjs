package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ClearBreakpointResponse extends AbstractResponse<ClearBreakpointRequest> {

    public ClearBreakpointResponse(ClearBreakpointRequest request, boolean success, boolean running) {
        super(request, success, running);
    }

    public long getBreakpoint() {
        return getRequest().getBreakpoint();
    }
}
