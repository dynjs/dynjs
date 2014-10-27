package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ClearBreakpointResponse extends AbstractResponse {

    public ClearBreakpointResponse(boolean success, boolean running) {
        super("clearbreakpoint", success, running);
    }
}
