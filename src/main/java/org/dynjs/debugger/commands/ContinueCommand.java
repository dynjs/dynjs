package org.dynjs.debugger.commands;

import io.netty.channel.ChannelHandler;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.handlers.ContinueHandler;
import org.dynjs.debugger.requests.ContinueRequest;
import org.dynjs.debugger.requests.ContinueResponse;

/**
 * @author Bob McWhirter
 */
public class ContinueCommand extends AbstractCommand<ContinueRequest, ContinueResponse> {

    public ContinueCommand(Debugger debugger) {
        super(debugger, ContinueRequest.class, ContinueResponse.class, ContinueHandler.class);
    }

    @Override
    public ContinueResponse handle(ContinueRequest request) {
        return null;
    }
}
