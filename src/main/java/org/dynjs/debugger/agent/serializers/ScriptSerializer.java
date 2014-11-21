package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.model.Script;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class ScriptSerializer extends StdSerializer<Script> {


    public ScriptSerializer() {
        super(Script.class);
    }

    @Override
    public void serialize(Script value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

        jgen.writeStartObject();

        jgen.writeStringField("type", "script");
        jgen.writeStringField("name", value.getName());
        jgen.writeNumberField("id", value.getId());
        if (value.isIncludeSource()) {
            jgen.writeStringField("source", value.getSource());
        }
        jgen.writeNumberField("lineOffset", 0);
        jgen.writeNumberField("columnOffset", 0);
        jgen.writeNumberField("sourceLength", value.getSourceLength());
        jgen.writeNumberField("lineCount", value.getLineCount());
        jgen.writeNumberField("scriptType", 2);
        jgen.writeNumberField("compilationType", 0);

        jgen.writeEndObject();

    }
}
