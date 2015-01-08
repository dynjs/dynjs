package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dynjs.debugger.requests.EvaluateResponse;
import org.dynjs.debugger.requests.LookupResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class LookupResponseSerializer extends DefaultResponseSerializer<LookupResponse> {

    public LookupResponseSerializer(HandleSerializer handleSerializer, AtomicInteger seqCounter) {
        super(LookupResponse.class, handleSerializer, seqCounter);
    }

    @Override
    public void serializeBody(LookupResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeFieldName( "body" );
        List<Object> objects = value.getList();

        jgen.writeStartObject();
        for ( Object each : objects ) {
            this.handleSerializer.serializeAsMapEntry( each, jgen, provider );
        }
        jgen.writeEndObject();
    }


}
