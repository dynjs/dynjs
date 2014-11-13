package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.ScriptsRequest;
import org.dynjs.debugger.requests.ScriptsResponse;

/**
 * @author Bob McWhirter
 */
public class Scripts extends AbstractCommand<ScriptsRequest, ScriptsResponse> {

    public Scripts(Debugger debugger) {
        super(debugger, ScriptsRequest.class, ScriptsResponse.class);
    }

    @Override
    public ScriptsResponse handle(ScriptsRequest request) {
        return new ScriptsResponse(request, true, true);
    }
}
