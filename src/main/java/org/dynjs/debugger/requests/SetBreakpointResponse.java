package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class SetBreakpointResponse extends AbstractResponse {

    public SetBreakpointResponse(boolean success, boolean running) {
        super("setbreakpoint", success, running);
    }
}
