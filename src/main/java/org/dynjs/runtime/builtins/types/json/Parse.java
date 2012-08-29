package org.dynjs.runtime.builtins.types.json;

import java.io.IOException;
import java.util.List;

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
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
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
        Object unfiltered = null;
        try {
            JsonParser parser = factory.createJsonParser(jsonText);
            parser.nextToken();
            unfiltered = parse(context, parser);
        } catch (IOException e) {
            throw new DynJSException(e);
        }

        Object reviver = args[1];

        if (!Types.isCallable(reviver)) {
            return unfiltered;
        }

        JSObject root = BuiltinObject.newObject(context);
        root.put(context, "", unfiltered, false);

        return walk(context, (JSFunction) reviver, root, "");
    }

    protected Object walk(ExecutionContext context, JSFunction reviver, JSObject holder, String name) {
        Object val = holder.get(context, name);

        if (val instanceof JSObject) {
            JSObject jsVal = (JSObject) val;
            if (jsVal.getClassName().equals("Array")) {
                int len = Types.toInteger(jsVal.get(context, "length"));
                int i = 0;
                while ( i < len ) {
                    Object newElement = walk( context, reviver, jsVal, "" + i );
                    if ( newElement == Types.UNDEFINED ) {
                        jsVal.delete(context, "" + i, false );
                    } else {
                        jsVal.put(context, "" +i, newElement, false);
                    }
                    ++i;
                }
            } else {
                NameEnumerator keys = jsVal.getOwnPropertyNames();
                
                while ( keys.hasNext() ) {
                    String key = keys.next();
                    Object newElement = walk( context, reviver, jsVal, key);
                    if ( newElement == Types.UNDEFINED ) {
                        jsVal.delete( context, key, false );
                    } else {
                        jsVal.put( context, key, newElement, false );
                    }
                }
                
            }
        }
        
        return context.call(reviver, holder, name, val );
    }

    protected Object parse(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.START_ARRAY) {
            return parseArray(context, p);
        } else if (t == JsonToken.START_OBJECT) {
            return parseObject(context, p);
        } else {
            return parseValue(context, p);
        }
    }

    protected Object parseArray(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        DynArray array = BuiltinArray.newArray(context);
        int i = 0;
        while (p.nextToken() != JsonToken.END_ARRAY) {
            Object value = parse(context, p);
            array.put(context, "" + i, value, false);
            ++i;
        }

        return array;
    }

    protected Object parseObject(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        DynObject obj = BuiltinObject.newObject(context);

        while (p.nextToken() != JsonToken.END_OBJECT) {
            String name = p.getCurrentName();
            Object value = parse(context, p);
            obj.put(context, name, value, false);
        }
        return obj;
    }

    protected Object parseValue(ExecutionContext context, JsonParser p) throws JsonParseException, IOException {
        JsonToken t = p.getCurrentToken();

        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        } else if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        } else if (t == JsonToken.VALUE_NULL) {
            return Types.NULL;
        } else if (t == JsonToken.VALUE_STRING) {
            return p.getText();
        } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
            return (double) p.getFloatValue();
        } else if (t == JsonToken.VALUE_NUMBER_INT) {
            return p.getIntValue();
        }

        return Types.NULL;
    }

}
