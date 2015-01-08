package org.dynjs.runtime.builtins.types.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinObject;

import java.io.IOException;

public class Parse extends AbstractNativeFunction {

    public Parse(GlobalContext globalContext) {
        super(globalContext, true, "text", "reviver");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String jsonText = Types.toString(context, args[0]);
        
        JsonFactory factory = new JsonFactory();
        Object unfiltered = null;
        try {
            JsonParser parser = factory.createJsonParser(jsonText);
            parser.nextToken();
            unfiltered = parse(context, parser);
            if (parser.nextToken() != null) {
                throw new ThrowException(context, context.createSyntaxError("unexpected token"));
            }
        } catch (IOException e) {
            JSObject error = context.createSyntaxError(e.getMessage());
            throw new ThrowException(context, error);
        }

        Object reviver = args[1];

        if (!Types.isCallable(reviver)) {
            return unfiltered;
        }

        JSObject root = BuiltinObject.newObject(context);
        root.put(context, "", unfiltered, false);

        Object result = walk(context, (JSFunction) reviver, root, "");
        return result;
    }

    protected Object walk(ExecutionContext context, JSFunction reviver, JSObject holder, String name) {
        Object val = holder.get(context, name);

        if (val instanceof JSObject) {
            JSObject jsVal = (JSObject) val;
            if (jsVal.getClassName().equals("Array")) {
                long len = Types.toInteger(context, jsVal.get(context, "length"));
                long i = 0;
                while (i < len) {
                    Object newElement = walk(context, reviver, jsVal, "" + i);
                    if (newElement == Types.UNDEFINED) {
                        jsVal.delete(context, "" + i, false);
                    } else {
                        jsVal.put(context, "" + i, newElement, false);
                    }
                    ++i;
                }
            } else {
                NameEnumerator keys = jsVal.getOwnPropertyNames();

                while (keys.hasNext()) {
                    String key = keys.next();
                    Object newElement = walk(context, reviver, jsVal, key);
                    if (newElement == Types.UNDEFINED) {
                        jsVal.delete(context, key, false);
                    } else {
                        jsVal.put(context, key, newElement, false);
                    }
                }

            }
        }

        return context.call(reviver, holder, name, val);
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
        JSObject array = BuiltinArray.newArray(context);
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
            obj.defineOwnProperty(context, name,
                    PropertyDescriptor.newDataPropertyDescriptor(value, true, true, true), false);
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
        } else if (t == JsonToken.VALUE_NUMBER_FLOAT || t == JsonToken.VALUE_NUMBER_INT) {
            return p.getNumberValue();
        }

        return Types.NULL;
    }

}
