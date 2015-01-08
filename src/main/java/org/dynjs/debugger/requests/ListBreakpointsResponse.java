package org.dynjs.debugger.requests;

import org.dynjs.debugger.model.Breakpoint;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ListBreakpointsResponse extends AbstractResponse<ListBreakpointsRequest> {

    private final List<Breakpoint> breakPoints;

    public ListBreakpointsResponse(ListBreakpointsRequest request, List<Breakpoint> breakPoints, boolean success, boolean running) {
        super(request, success, running);
        this.breakPoints = breakPoints;
    }

    public List<Breakpoint> getBreakpoints() {
        return this.breakPoints;
    }
}
