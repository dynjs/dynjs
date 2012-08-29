package org.dynjs.runtime.builtins.types.json;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.dynjs.exception.DynJSException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinObject;

public class Parse extends AbstractNativeFunction {

    public Parse(GlobalObject globalObject) {
        super(globalObject, true, "text", "reviver");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String jsonText = Types.toString(args[0]);
        JsonFactory factory = new JsonFactory();
        Object result = null;
        try {
            JsonParser parser = factory.createJsonParser(jsonText);
            parser.nextToken();
            result = parse( context, parser );
        } catch (IOException e) {
            throw new DynJSException(e);
        }
        return result;
    }
    
    protected Object parse(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        JsonToken t = p.getCurrentToken();
        if ( t == JsonToken.START_ARRAY ) {
            return parseArray( context, p );
        } else if ( t == JsonToken.START_OBJECT ) {
            return parseObject( context, p );
        } else {
            return parseValue( context, p );
        }     
    }
    
    protected Object parseArray(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        DynArray array = BuiltinArray.newArray(context);
        int i = 0;
        while ( p.nextToken() != JsonToken.END_ARRAY ) {
            Object value = parse( context, p );
            array.put(context, ""+i, value, false);
            ++i;
        }
        
        return array;
    }
    
    protected Object parseObject(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        DynObject obj = BuiltinObject.newObject(context);
        
        while ( p.nextToken() != JsonToken.END_OBJECT ) {
            String name = p.getCurrentName();
            Object value = parse( context, p );
            obj.put(context, name, value, false);
        }
        return obj;
    }
    
    protected Object parseValue(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        JsonToken t = p.getCurrentToken();
        
        if ( t == JsonToken.VALUE_FALSE ) {
            return Boolean.FALSE;
        } else if ( t == JsonToken.VALUE_TRUE ) {
            return Boolean.TRUE;
        } else if ( t == JsonToken.VALUE_NULL ) {
            return Types.NULL;
        } else if ( t == JsonToken.VALUE_STRING ) {
            return p.getText();
        } else if ( t == JsonToken.VALUE_NUMBER_FLOAT ) {
            return (double) p.getFloatValue();
        } else if ( t == JsonToken.VALUE_NUMBER_INT ) {
            return p.getIntValue();
        }
        
        return Types.NULL;
    }

}
