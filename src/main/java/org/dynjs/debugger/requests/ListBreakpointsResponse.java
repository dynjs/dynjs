package org.dynjs.debugger.requests;

import org.dynjs.debugger.BreakPoint;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ListBreakpointsResponse extends AbstractResponse<ListBreakpointsRequest> {

    private final List<BreakPoint> breakPoints;

    public ListBreakpointsResponse(ListBreakpointsRequest request, List<BreakPoint> breakPoints, boolean success, boolean running) {
        super(request, success, running);
        this.breakPoints = breakPoints;
    }

    public List<BreakPoint> getBreakpoints() {
        return this.breakPoints;
    }
}
