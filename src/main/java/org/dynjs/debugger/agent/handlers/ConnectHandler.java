package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.events.ConnectEvent;
import org.dynjs.debugger.requests.Response;

/**
 * @author Bob McWhirter
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {

    public ConnectHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ConnectEvent());
        super.channelActive(ctx);
    }
}
