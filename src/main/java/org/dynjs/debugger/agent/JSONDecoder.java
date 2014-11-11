package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.commands.AbstractCommand;
import org.dynjs.debugger.requests.Request;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class JSONDecoder extends ReplayingDecoder<JSONDecoder.State> {

    private static String CONTENT_LENGTH = "Content-Length:";
    private static Charset UTF8 = Charset.forName("UTF8");

    private final Debugger debugger;
    private int length;

    public enum State {
        HEADER,
        BODY,
    }

    public JSONDecoder(Debugger debugger) {
        super(State.HEADER);
        this.debugger = debugger;
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
            }
        }

        if ( this.length < 0 ) {
            System.err.println( "LEN LESS THAN 0" );
        }

        if (state() == State.BODY) {
            if (in.readableBytes() < this.length) {
                return;
            }

            ByteBuf jsonBuf = in.slice(in.readerIndex(), this.length);
            in.readerIndex(in.readerIndex() + this.length);
            this.length = -1;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true );
            JsonNode node = mapper.readTree(new ByteBufInputStream((ByteBuf) jsonBuf));
            String type = node.get("type").asText();
            if ("request".equals(type)) {
                String commandStr = node.get("command").asText();
                AbstractCommand command = this.debugger.getCommand(commandStr);
                if (command != null) {
                    ObjectReader reader = mapper.reader().withValueToUpdate(command.newRequest());
                    Request request = reader.readValue(node);
                    out.add(request);
                }
            }
            state( State.HEADER );
        }
    }

}
