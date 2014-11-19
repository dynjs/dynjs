package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.ScriptsResponse;
import org.dynjs.runtime.SourceProvider;

import java.io.IOException;
import java.util.Collection;

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

        Collection<SourceProvider> scripts = value.getScripts();
        for ( SourceProvider each : scripts ) {
            jgen.writeStartObject();
            this.handleSerializer.serializeScript(each, value.isIncludeSource(), jgen, provider);
            jgen.writeEndObject();
        }

        jgen.writeEndArray();
    }


}
