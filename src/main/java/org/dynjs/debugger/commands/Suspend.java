package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.SuspendRequest;
import org.dynjs.debugger.requests.SuspendResponse;
import org.dynjs.debugger.requests.VersionRequest;
import org.dynjs.debugger.requests.VersionResponse;

/**
 * @author Bob McWhirter
 */
public class Suspend extends AbstractCommand<SuspendRequest, SuspendResponse> {

    public Suspend(Debugger debugger) {
        super(debugger, SuspendRequest.class, SuspendResponse.class);
    }

    @Override
    public SuspendResponse handle(SuspendRequest request) {
        this.debugger.suspend();
        return new SuspendResponse(request, true, this.debugger.isRunning());
    }
}
