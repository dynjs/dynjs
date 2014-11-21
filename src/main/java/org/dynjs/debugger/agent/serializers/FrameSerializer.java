package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.model.Frame;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class FrameSerializer extends StdSerializer<Frame> {

    private final HandleSerializer handleSerializer;

    public FrameSerializer(HandleSerializer handleSerializer) {
        super(Frame.class);
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(Frame value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeNumberField("index", value.getIndex());
        jgen.writeNumberField("line", value.getLine() - 1);
        jgen.writeNumberField("column", value.getColumn());
        jgen.writeBooleanField("constructCall", value.isConstructor());

        //provider.defaultSerializeField( "scopes", value.getScopes(), jgen );
        //provider.defaultSerializeField( "func", value.getFunc(), jgen );

        if (value.getFunc() != null) {
            if (value.getFunc().getSource() != null) {
                jgen.writeFieldName("script");
                this.handleSerializer.serializeRef(value.getFunc().getSource(), jgen, provider);
            }

            jgen.writeFieldName("func");
            this.handleSerializer.serializeRef(value.getFunc(), jgen, provider);
        }

        jgen.writeFieldName("receiver");
        this.handleSerializer.serializeRef(value.getReceiver(), jgen, provider);

        jgen.writeFieldName("scopes");
        jgen.writeStartArray();
        jgen.writeEndArray();

        jgen.writeEndObject();
    }


}
