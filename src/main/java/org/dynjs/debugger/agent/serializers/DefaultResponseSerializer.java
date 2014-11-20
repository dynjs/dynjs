package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.dynjs.debugger.requests.ListResponse;
import org.dynjs.debugger.requests.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class DefaultResponseSerializer<T extends Response> extends StdSerializer<T> {

    private final AtomicInteger seqCounter;
    protected final HandleSerializer handleSerializer;

    public DefaultResponseSerializer(HandleSerializer handleSerializer, AtomicInteger seqCounter) {
        this((Class<T>) Response.class, handleSerializer, seqCounter);

    }

    protected DefaultResponseSerializer(Class<T> cls, HandleSerializer handleSerializer, AtomicInteger seqCounter) {
        super(cls);
        this.seqCounter = seqCounter;
        this.handleSerializer = handleSerializer;
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

        jgen.writeStartObject();
        jgen.writeStringField("type", "response");
        jgen.writeStringField("command", value.getRequest().getCommand());
        jgen.writeNumberField("seq", this.seqCounter.incrementAndGet());
        jgen.writeNumberField("request_seq", value.getRequest().getSeq());
        jgen.writeBooleanField("success", value.isSuccess());
        jgen.writeBooleanField("running", value.isRunning());

        serializeBody(value, jgen, provider);

        jgen.writeFieldName( "refs" );
        jgen.writeStartArray();
        for ( Object each : value.getRefs() ) {
            this.handleSerializer.serialize( each, jgen, provider );
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }

    protected void serializeBody(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

        if (value instanceof ListResponse) {
            jgen.writeFieldName("body");
            provider.defaultSerializeValue(((ListResponse) value).getValues(), jgen);
        } else {
            JavaType type = provider.getTypeFactory().constructType(value.getClass());
            BeanDescription desc = provider.getConfig().introspect(type);
            JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, type, desc);
            if ( serializer != null ) {
                jgen.writeFieldName("body");
                serializer.serialize(value, jgen, provider);
            }
        }
    }
}
