package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.SetBreakpointRequest;
import org.dynjs.debugger.requests.SetBreakpointResponse;

/**
 * @author Bob McWhirter
 */
public class SetBreakpoint extends AbstractCommand<SetBreakpointRequest, SetBreakpointResponse> {

    public SetBreakpoint(Debugger debugger) {
        super(debugger, SetBreakpointRequest.class, SetBreakpointResponse.class);
    }

    @Override
    public SetBreakpointResponse handle(SetBreakpointRequest request) {

        String type = request.getArguments().getType();

        if ( type.equals( "scriptRegExp" ) ) {
            long num = this.debugger.setRegexpBreakPoint( request.getArguments().getTarget(), request.getArguments().getLine() );
            return new SetBreakpointResponse( request, num, true, false );
        } else if ( type.equals( "function" ) ) {

        }

        return new SetBreakpointResponse( request, -1, false, false );
    }
}
