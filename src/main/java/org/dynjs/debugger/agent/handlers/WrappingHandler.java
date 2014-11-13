package org.dynjs.debugger.agent.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.dynjs.debugger.events.Event;
import org.dynjs.debugger.events.EventWrapper;
import org.dynjs.debugger.requests.Response;
import org.dynjs.debugger.requests.ResponseWrapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class WrappingHandler extends ChannelOutboundHandlerAdapter {

    public static AtomicInteger seqCounter = new AtomicInteger();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof Response ) {
            ResponseWrapper wrapper = new ResponseWrapper((Response) msg);
            super.write(ctx, wrapper, promise);
        } else if ( msg instanceof Event) {
            EventWrapper wrapper = new EventWrapper((Event) msg);
            super.write(ctx, wrapper,promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
