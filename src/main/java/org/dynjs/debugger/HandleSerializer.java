package org.dynjs.debugger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dynjs.debugger.requests.EvaluateResponse;
import org.dynjs.runtime.Types;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class HandleSerializer extends JsonSerializer<EvaluateResponse> {

    @Override
    public void serialize(EvaluateResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Object result = value.getResult();

        jgen.writeStartObject();
        provider.defaultSerializeField("command", value.getCommand(), jgen);
        provider.defaultSerializeField("running", value.isRunning(), jgen);
        provider.defaultSerializeField("success", value.isSuccess(), jgen);

        if (result == Types.UNDEFINED) {
            jgen.writeStringField("type", "undefined");
        } else if (result == Types.NULL) {
            jgen.writeStringField("type", "null");
        } else if (result instanceof Boolean) {
            jgen.writeStringField("type", "boolean");
            jgen.writeBooleanField( "value", (Boolean) result);
        } else if (result instanceof Double) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField( "value", (Double) result);
        } else if (result instanceof Long) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField( "value", (Long) result);
        } else if (result instanceof String) {
            jgen.writeStringField("type", "string");
            jgen.writeStringField( "value", (String) result);
        }

        jgen.writeEndObject();
    }
}
