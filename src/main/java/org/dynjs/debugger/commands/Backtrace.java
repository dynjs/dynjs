package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.model.Frame;
import org.dynjs.debugger.requests.BacktraceRequest;
import org.dynjs.debugger.requests.BacktraceResponse;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class Backtrace extends AbstractCommand<BacktraceRequest, BacktraceResponse> {

    public Backtrace(Debugger debugger) {
        super(debugger, BacktraceRequest.class, BacktraceResponse.class);
    }

    @Override
    public BacktraceResponse handle(BacktraceRequest request) {
        List<Frame> frames = this.debugger.getFrames( request.getFromFrame(), request.getToFrame() );
        return new BacktraceResponse(request, frames, true, this.debugger.isRunning() );
    }
}
