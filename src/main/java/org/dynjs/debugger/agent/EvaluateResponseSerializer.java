package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.EvaluateResponse;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class EvaluateResponseSerializer extends StdSerializer<EvaluateResponse> {

    private final HandleSerializer handleSerializer;

    EvaluateResponseSerializer(HandleSerializer handleSerializer) {
        super(EvaluateResponse.class);
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(EvaluateResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        Object result = value.getResult();

        jgen.writeStartObject();
        //provider.defaultSerializeField("command", value.getCommand(), jgen);
        //provider.defaultSerializeField("running", value.isRunning(), jgen);
        //provider.defaultSerializeField("success", value.isSuccess(), jgen);
        //provider.defaultSerializeField("request_seq", value.getRequest().getSeq(), jgen );

        this.handleSerializer.serializeBody(result, jgen, provider);

        jgen.writeEndObject();
    }


}
