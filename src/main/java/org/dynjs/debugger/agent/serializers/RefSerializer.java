package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dynjs.debugger.Debugger;
import org.dynjs.runtime.*;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class RefSerializer extends JsonSerializer<Object> {

    private Debugger debugger;

    public RefSerializer(Debugger debugger) {
        super();
        this.debugger = debugger;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        serializeBody(value, jgen, provider);
        jgen.writeEndObject();
    }

    public void serializeBody(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int ref = this.debugger.getReferenceManager().getReference(value);
        jgen.writeNumberField("ref", ref);
        if (value == Types.UNDEFINED) {
            jgen.writeStringField("type", "undefined");
        } else if (value == Types.NULL) {
            jgen.writeStringField("type", "null");
        } else if (value instanceof Boolean) {
            jgen.writeStringField("type", "boolean");
            jgen.writeBooleanField("value", (Boolean) value);
        } else if (value instanceof Double) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField("value", (Double) value);
        } else if (value instanceof Long) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField("value", (Long) value);
        } else if (value instanceof String) {
            jgen.writeStringField("type", "string");
            jgen.writeStringField("value", (String) value);
        } else if (value instanceof JSObject) {
            jgen.writeStringField("type", "object");
        }

    }

}

