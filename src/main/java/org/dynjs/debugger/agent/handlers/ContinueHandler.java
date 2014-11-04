package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.dynjs.debugger.requests.ContinueRequest;

/**
 * @author Bob McWhirter
 */
public class ContinueHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( msg instanceof ContinueRequest ) {

        } else {
            super.channelRead(ctx, msg);
        }
    }
}
