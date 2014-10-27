package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ChangeBreakpointResponse extends AbstractResponse {

    public ChangeBreakpointResponse(boolean success, boolean running) {
        super("changebreakpoint", success, running);
    }
}
