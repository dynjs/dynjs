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

        provider.defaultSerializeField( "index", value.getIndex(), jgen );
        provider.defaultSerializeField( "line", value.getLine(), jgen );
        provider.defaultSerializeField( "column", value.getColumn(), jgen );
        provider.defaultSerializeField( "scopes", value.getScopes(), jgen );
        provider.defaultSerializeField( "func", value.getFunc(), jgen );

        jgen.writeFieldName( "receiver" );
        this.handleSerializer.serializeRef( value.getReceiver(), jgen, provider );

        jgen.writeEndObject();
    }


}
