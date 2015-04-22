package org.dynjs.debugger.agent.serializers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.dynjs.debugger.events.Event;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bob McWhirter
 */
public class EventSerializer extends StdSerializer<Event> {

    private final AtomicInteger seqCounter;

    public EventSerializer(AtomicInteger seqCounter) {
        super(Event.class);
        this.seqCounter = seqCounter;
    }

    @Override
    public void serialize(Event value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "event");
        jgen.writeStringField("event", value.getEvent() );
        jgen.writeNumberField("seq", this.seqCounter.incrementAndGet());
        jgen.writeFieldName("body");

        JavaType type = provider.getConfig().getTypeFactory().constructType(value.getClass());
        BeanDescription desc = provider.getConfig().introspect(type);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, type, desc);
        serializer.serialize(value, jgen, provider);


        jgen.writeEndObject();

    }
}
