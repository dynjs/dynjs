package org.dynjs.debugger.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.commands.AbstractCommand;
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

    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(new ByteBufInputStream((ByteBuf) msg));
            String type = node.get("type").asText();
            if ("command".equals(type)) {
                String commandStr = node.get("command").asText();
                AbstractCommand command = this.debugger.getCommand(commandStr);
                if (command != null) {
                    ObjectReader reader = mapper.reader().withValueToUpdate( command.newRequest() );
                    Request request = reader.readValue(node);
                    super.channelRead( ctx, request );
                    return;
                }
            }
        }
        super.channelRead(ctx, msg);
    }
    */

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof ResponseWrapper || msg instanceof EventWrapper) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(msg);
                json += "\r\n";
                byte[] jsonBytes = json.getBytes( UTF8 );

                String headers = "Content-Length: " + jsonBytes.length + "\r\n\r\n";
                ByteBuf buffer = ctx.alloc().buffer();
                buffer.writeBytes(headers.getBytes( UTF8 ));
                buffer.writeBytes(jsonBytes);
                super.write(ctx, buffer, promise);
            } catch ( Throwable t) {
                t.printStackTrace();
            }
        } else {
            super.write( ctx, msg, promise );
        }

    }
}
