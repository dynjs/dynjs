package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public class ListBreakpointsResponse extends AbstractResponse {

    public ListBreakpointsResponse(boolean success, boolean running) {
        super("listbreakpoints", success, running);
    }
}
