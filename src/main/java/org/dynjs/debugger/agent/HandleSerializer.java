package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dynjs.debugger.requests.EvaluateResponse;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.Types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class HandleSerializer extends StdSerializer<EvaluateResponse> {

    private Map<Integer,Object> refs = new HashMap<>();
    private Map<Object,Integer> objects = new HashMap<>();

    private int refCounter = 0;

    HandleSerializer() {
        super(EvaluateResponse.class);
    }

    @Override
    public void serialize(EvaluateResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        Object result = value.getResult();

        jgen.writeStartObject();
        provider.defaultSerializeField("command", value.getCommand(), jgen);
        provider.defaultSerializeField("running", value.isRunning(), jgen);
        provider.defaultSerializeField("success", value.isSuccess(), jgen);

        int ref = getRef( result );
        jgen.writeNumberField( "ref", ref );

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
            jgen.writeNumberField("value", (Long) result);
        } else if (result instanceof String) {
            jgen.writeStringField("type", "string");
            jgen.writeStringField( "value", (String) result);
        } else if ( result instanceof JSObject) {
            jgen.writeStringField("type", "object");
            serialize((JSObject)result, jgen, provider );

        }

        jgen.writeEndObject();
    }

    private void serialize(JSObject result, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeFieldName( "properties" );

        jgen.writeStartArray();

        NameEnumerator enumerator = result.getAllEnumerablePropertyNames();

        while ( enumerator.hasNext() ) {
            String name = enumerator.next();
            Object value = result.get( null, name );
            jgen.writeStartObject();
            jgen.writeStringField("name", name);
            jgen.writeNumberField("ref", getRef(value));
            jgen.writeEndObject();
        }

        jgen.writeEndArray();

    }

    private int getRef(Object object) {
        Integer ref = this.objects.get( object );
        if ( ref != null ) {
            return ref;
        }

        ref = ++this.refCounter;

        this.objects.put( object, ref );
        this.refs.put( ref, object );

        return ref;

    }

}
