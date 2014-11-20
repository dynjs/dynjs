package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.agent.serializers.*;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.requests.NoArgumentsRequest;
import org.dynjs.debugger.requests.Request;
import org.dynjs.debugger.requests.ScriptsRequest;
import org.dynjs.debugger.requests.SourceRequest;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class JSONDecoder extends ReplayingDecoder<JSONDecoder.State> {

    private static String CONTENT_LENGTH = "Content-Length:";
    private static Charset UTF8 = Charset.forName("UTF8");

    private final Debugger debugger;
    private final ObjectMapper mapper;
    private int length;

    public enum State {
        HEADER,
        BODY,
    }

    public JSONDecoder(Debugger debugger) {
        super(State.HEADER);
        this.debugger = debugger;

        SimpleModule module = new SimpleModule();

        module.addDeserializer(SourceRequest.class, new SourceRequestDeserializer());

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(module);
        this.mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (state() == State.HEADER) {
            int nLoc = in.bytesBefore((byte) '\n');

            ByteBuf headerBuf = in.readBytes(nLoc + 1);
            String headerLine = headerBuf.toString(UTF8).trim();

            nLoc = in.bytesBefore((byte) '\n');
            in.readBytes(nLoc);
            in.readByte();

            if (headerLine.startsWith(CONTENT_LENGTH)) {
                String lenStr = headerLine.substring(CONTENT_LENGTH.length()).trim();
                this.length = Integer.parseInt(lenStr);
                state(State.BODY);
                return;
            }
        }

        if (state() == State.BODY) {
            JsonNode node = mapper.readTree(new ByteBufInputStream(in, this.length));
            String type = node.get("type").asText();
            if ("request".equals(type)) {
                String commandStr = node.get("command").asText();
                AbstractCommand command = this.debugger.getCommand(commandStr);
                if (command != null) {
                    Request request = command.newRequest();
                    request.setSeq( node.get( "seq" ).asInt() );
                    ObjectReader reader = mapper.reader().withValueToUpdate(request);
                    if ( node.has("arguments" ) ) {
                        reader.readValue(node.get("arguments"));
                    } else if ( request instanceof NoArgumentsRequest ) {
                        reader.readValue(node);
                    }
                    out.add(request);
                }
            }
            state(State.HEADER);
            this.length = -1;
        }
    }
}
