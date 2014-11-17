package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.ScriptsResponse;
import org.dynjs.runtime.SourceProvider;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class ScriptsResponseSerializer extends StdSerializer<ScriptsResponse> {

    private final HandleSerializer handleSerializer;

    public ScriptsResponseSerializer(HandleSerializer handleSerializer) {
        super(ScriptsResponse.class);
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(ScriptsResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartArray();

        for ( SourceProvider each : value.getScripts() )  {
            this.handleSerializer.serializeScript( each, value.isIncludeSource(), jgen, provider );
        }

        jgen.writeEndArray();
    }


}
