package org.dynjs.debugger.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.events.ConnectEvent;
import org.dynjs.debugger.events.Event;
import org.dynjs.debugger.events.EventWrapper;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.Response;
import org.dynjs.debugger.requests.ResponseWrapper;

import java.nio.charset.Charset;

/**
 * @author Bob McWhirter
 */
public class JSONEncoder extends ChannelDuplexHandler {

    private static final Charset UTF8 = Charset.forName( "UTF8" );

    private Debugger debugger;

    public JSONEncoder(Debugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof ResponseWrapper || msg instanceof EventWrapper ) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                String json = mapper.writeValueAsString(msg);
                json += "\r\n";
                byte[] jsonBytes = json.getBytes(UTF8);

                String headers = "Content-Length: " + jsonBytes.length + "\r\n\r\n";
                ByteBuf buffer = ctx.alloc().buffer();
                buffer.writeBytes(headers.getBytes(UTF8));
                buffer.writeBytes(jsonBytes);
                super.write(ctx, buffer, promise);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else if ( msg instanceof ConnectEvent ) {
            String headers = "Type: connect\r\nContent-Length: 0\r\n\r\n";
            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(headers.getBytes(UTF8));
            super.write( ctx, buffer, promise );
        } else {
            super.write( ctx, msg, promise );
        }

    }
}
