package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.ListBreakpointsRequest;
import org.dynjs.debugger.requests.ListBreakpointsResponse;
import org.dynjs.debugger.requests.SetBreakpointRequest;
import org.dynjs.debugger.requests.SetBreakpointResponse;

/**
 * @author Bob McWhirter
 */
public class ListBreakpoints extends AbstractCommand<ListBreakpointsRequest, ListBreakpointsResponse> {

    public ListBreakpoints(Debugger debugger) {
        super(debugger, ListBreakpointsRequest.class, ListBreakpointsResponse.class);
    }

    @Override
    public ListBreakpointsResponse handle(ListBreakpointsRequest request) {
        return new ListBreakpointsResponse( request, this.debugger.getBreakPoints(), true, false );
    }
}
