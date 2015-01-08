package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.dynjs.debugger.requests.SourceRequest;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class SourceRequestDeserializer extends StdDeserializer<SourceRequest> {

    public SourceRequestDeserializer() {
        super(SourceRequest.class);
    }

    @Override
    public SourceRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deserialize(jp, ctxt, new SourceRequest());
    }

    @Override
    public SourceRequest deserialize(JsonParser jp, DeserializationContext ctxt, SourceRequest intoValue) throws IOException, JsonProcessingException {
        TreeNode tree = jp.readValueAsTree();
        intoValue.setFromLine(((JsonNode) tree.get("fromLine")).asInt());
        intoValue.setToLine(((JsonNode) tree.get("toLine")).asInt());
        return intoValue;
    }
}
