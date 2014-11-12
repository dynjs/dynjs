package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.ClearBreakpointRequest;
import org.dynjs.debugger.requests.ClearBreakpointResponse;
import org.dynjs.debugger.requests.ListBreakpointsRequest;
import org.dynjs.debugger.requests.ListBreakpointsResponse;

/**
 * @author Bob McWhirter
 */
public class ClearBreakpoint extends AbstractCommand<ClearBreakpointRequest, ClearBreakpointResponse> {

    public ClearBreakpoint(Debugger debugger) {
        super(debugger, ClearBreakpointRequest.class, ClearBreakpointResponse.class);
    }

    @Override
    public ClearBreakpointResponse handle(ClearBreakpointRequest request) {
        if ( this.debugger.removeBreakpoint( request.getArguments().getBreakpoint() ) ) {
            return new ClearBreakpointResponse( request, true, false );
        } else {
            return new ClearBreakpointResponse( request, false, false );
        }
    }
}
