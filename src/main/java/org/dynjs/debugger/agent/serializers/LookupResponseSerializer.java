package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.LookupResponse;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class LookupResponseSerializer extends StdSerializer<LookupResponse> {

    private final HandleSerializer handleSerializer;

    public LookupResponseSerializer(HandleSerializer handleSerializer) {
        super(LookupResponse.class);
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(LookupResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        for ( Object each : value.getList() ) {
            this.handleSerializer.serializeAsMapEntry( each, jgen, provider );
        }

        jgen.writeEndObject();
    }


}
