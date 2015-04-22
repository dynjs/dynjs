package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dynjs.debugger.requests.EvaluateResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class EvaluateResponseSerializer extends DefaultResponseSerializer<EvaluateResponse> {

    public EvaluateResponseSerializer(HandleSerializer handleSerializer, AtomicInteger seqCounter) {
        super(EvaluateResponse.class, handleSerializer, seqCounter);
    }

    @Override
    public void serializeBody(EvaluateResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Object result = value.getResult();
        jgen.writeFieldName( "body" );
        this.handleSerializer.serialize(result, jgen, provider);
    }


}
