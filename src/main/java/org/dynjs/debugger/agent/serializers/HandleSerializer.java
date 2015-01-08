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
public class HandleSerializer extends JsonSerializer<Object> {

    private Debugger debugger;

    public HandleSerializer(Debugger debugger) {
        super();
        this.debugger = debugger;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        serializeBody(value, jgen, provider);
        jgen.writeEndObject();
    }

    public void serializeRef(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        int ref = this.debugger.getReferenceManager().getReference( value );
        jgen.writeNumberField( "ref", ref );
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
        } else if ( value instanceof JSFunction ) {
            jgen.writeStringField("type", "function");
            Object name = ((JSFunction) value).get( null, "name" );
            if ( name != Types.UNDEFINED ) {
                jgen.writeStringField( "name", name.toString() );
            } else {
                jgen.writeStringField( "name", "" );
            }
            jgen.writeStringField( "inferredName", "" );
            SourceProvider source = ((JSFunction) value).getSource();
            if ( source != null ) {
                jgen.writeNumberField( "scriptId", source.getId() );
            }
        } else if (value instanceof JSObject) {
            jgen.writeStringField("type", "object");
        } else if ( value instanceof SourceProvider ) {
            jgen.writeStringField("type", "script");
        }
        jgen.writeEndObject();

    }

    public void serializeAsMapEntry(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int ref = this.debugger.getReferenceManager().getReference(value);
        jgen.writeFieldName("" + ref);
        serialize(value, jgen, provider);
    }

    public void serializeBody(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int ref = this.debugger.getReferenceManager().getReference(value);
        jgen.writeNumberField("handle", ref);

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
            serializeJSObject((JSObject) value, jgen, provider);
        } else if (value instanceof SourceProvider) {
            serializeScript((SourceProvider) value, false, jgen, provider);
        }
    }

    public void serializeScript(SourceProvider value, boolean includeSource, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStringField("type", "script");
        jgen.writeStringField("name", value.getName());
        jgen.writeNumberField("id", value.getId());
        if (includeSource) {
            jgen.writeStringField("source", value.getSource());
        }
        jgen.writeNumberField("sourceLength", value.getSourceLength());
        jgen.writeNumberField("lineCount", value.getLineCount());
        jgen.writeNumberField("scriptType", 2);
        jgen.writeNumberField("compilationType", 0);
    }

    private void serializeJSObject(JSObject result, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeFieldName("properties");

        jgen.writeStartArray();

        NameEnumerator enumerator = result.getAllEnumerablePropertyNames();

        while (enumerator.hasNext()) {
            String name = enumerator.next();
            Object propResult = result.getProperty(null, name);

            if (propResult == Types.UNDEFINED) {
                jgen.writeStartObject();
                jgen.writeStringField("name", name);
                jgen.writeNumberField("ref", this.debugger.getReferenceManager().getReference(Types.UNDEFINED));
                jgen.writeEndObject();
            } else {
                PropertyDescriptor prop = (PropertyDescriptor) propResult;

                if (prop.hasValue()) {

                    Object value = prop.getValue();
                    jgen.writeStartObject();
                    jgen.writeStringField("name", name);
                    jgen.writeNumberField("ref", this.debugger.getReferenceManager().getReference(prop.getValue()));

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
                    }
                    jgen.writeEndObject();

                } else {
                    // WHAT?
                }
            }
        }

        jgen.writeEndArray();

    }


}
