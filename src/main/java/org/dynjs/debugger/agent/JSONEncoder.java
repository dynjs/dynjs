package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.serializers.*;
import org.dynjs.debugger.events.ConnectEvent;
import org.dynjs.debugger.events.Event;
import org.dynjs.debugger.requests.Response;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class JSONEncoder extends ChannelDuplexHandler {

    private static final Charset UTF8 = Charset.forName("UTF8");
    private final ObjectMapper mapper;

    private Debugger debugger;

    public JSONEncoder(Debugger debugger) {
        this.debugger = debugger;

        HandleSerializer handleSerializer = new HandleSerializer(this.debugger);

        SimpleModule module = new SimpleModule();

        AtomicInteger seqCounter = new AtomicInteger(0);

        module.addSerializer(new EventSerializer(seqCounter));

        module.addSerializer(new DefaultResponseSerializer(handleSerializer, seqCounter));
        module.addSerializer(new EvaluateResponseSerializer(handleSerializer, seqCounter));
        module.addSerializer(new LookupResponseSerializer(handleSerializer, seqCounter));
        
        module.addSerializer(new FrameSerializer(handleSerializer));

        module.addSerializer(new ScriptSerializer());

        this.mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ConnectEvent) {
            String headers = "Type: connect\r\nContent-Length: 0\r\n\r\n";
            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(headers.getBytes(UTF8));
            super.write(ctx, buffer, promise);
        } else if (msg instanceof Response || msg instanceof Event) {
            String json = toJSON(msg);
            byte[] jsonBytes = json.getBytes(UTF8);
            String headers = "Content-Length: " + jsonBytes.length + "\r\n\r\n";
            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(headers.getBytes(UTF8));
            buffer.writeBytes(jsonBytes);
            super.write(ctx, buffer, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    protected String toJSON(Object msg) throws JsonProcessingException {
        try {
            String json = this.mapper.writeValueAsString(msg);
            json += "\r\n";
            return json;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "";
    }
}
