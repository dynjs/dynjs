package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.ResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ResponseWrapperSerializer extends StdSerializer<ResponseWrapper> {

    private final HandleSerializer handleSerializer;

    public ResponseWrapperSerializer(HandleSerializer handleSerializer) {
        super(ResponseWrapper.class);
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(ResponseWrapper value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        provider.defaultSerializeField( "type", value.getType(), jgen );
        provider.defaultSerializeField( "command", value.getCommand(), jgen );
        provider.defaultSerializeField( "running", value.isRunning(), jgen );
        provider.defaultSerializeField( "success", value.isSuccess(), jgen );
        provider.defaultSerializeField( "seq", value.getSeq(), jgen );
        provider.defaultSerializeField( "request_seq", value.getRequestSeq(), jgen );
        provider.defaultSerializeField( "body", value.getBody(), jgen );


        jgen.writeFieldName( "refs" );

        jgen.writeStartArray();
        Collection<? extends Object> refs = value.getRefs();
        for ( Object each : refs ) {
            this.handleSerializer.serialize(each, jgen, provider );
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }


}
