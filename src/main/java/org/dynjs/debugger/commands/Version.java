package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.VersionRequest;
import org.dynjs.debugger.requests.VersionResponse;

/**
 * @author Bob McWhirter
 */
public class Version extends AbstractCommand<VersionRequest, VersionResponse> {

    public Version(Debugger debugger) {
        super(debugger, VersionRequest.class, VersionResponse.class);
    }

    @Override
    public VersionResponse handle(VersionRequest request) {
        return new VersionResponse(request, true, this.debugger.isRunning() );
    }
}
