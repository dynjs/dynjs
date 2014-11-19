package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Bob McWhirter
 */
public class ErrorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println( "CAUGHT EXCEPTION: " + cause );
        cause.printStackTrace();
        //super.exceptionCaught(ctx, cause);
    }
}
