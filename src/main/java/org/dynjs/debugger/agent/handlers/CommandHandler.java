package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.requests.Response;

/**
 * @author Bob McWhirter
 */
public class CommandHandler extends ChannelInboundHandlerAdapter {

    private final AbstractCommand command;

    public CommandHandler(AbstractCommand command) {
        this.command = command;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( this.command.requestClass().isInstance(msg) ){
            Response result = this.command.handle((org.dynjs.debugger.requests.Request) msg);
            ctx.writeAndFlush(result);
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
