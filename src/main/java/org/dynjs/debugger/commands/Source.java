package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.handlers.SourceHandler;
import org.dynjs.debugger.requests.SourceRequest;
import org.dynjs.debugger.requests.SourceResponse;

/**
 * @author Bob McWhirter
 */
public class Source extends AbstractCommand<SourceRequest, SourceResponse> {

    public Source(Debugger debugger) {
        super(debugger, SourceRequest.class, SourceResponse.class, SourceHandler.class);
    }

    @Override
    public SourceResponse handle(SourceRequest request) {
        return null;
    }
}
