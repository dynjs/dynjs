package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.model.Breakpoint;
import org.dynjs.debugger.model.RegexpBreakpoint;
import org.dynjs.debugger.model.ScriptBreakpoint;
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

        String type = request.getType();

        String target = request.getTarget();
        int line = request.getLine();
        int column = request.getColumn();

        Breakpoint breakpoint = null;
        if ( type.equals( "scriptRegExp" ) ) {
            breakpoint = new RegexpBreakpoint( target, line, column );
        } else if ( type.equals( "script" ) ) {
            breakpoint = new ScriptBreakpoint( target, line, column );
        } else if ( type.equals( "function" ) ) {
            return new SetBreakpointResponse( request, -1, false, false );
        }

        this.debugger.setBreakpoint( breakpoint );
        return new SetBreakpointResponse( request, breakpoint.getNumber(), true, this.debugger.isRunning() );
    }
}
