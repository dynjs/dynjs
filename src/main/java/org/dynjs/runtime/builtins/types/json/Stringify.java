package org.dynjs.runtime.builtins.types.json;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinObject;

public class Stringify extends AbstractNativeFunction {

    public Stringify(GlobalContext globalContext) {
        super(globalContext, true, "value", "replacer", "space");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {

        List<Object> stack = new ArrayList<>();
        String indent = "";

        List<String> propertyList = null;
        JSFunction replacerFunction = null;

        Object replacer = args[1];
        if (replacer instanceof JSObject) {
            JSObject jsReplacer = (JSObject) replacer;
            if (jsReplacer instanceof JSFunction) {
                replacerFunction = (JSFunction) jsReplacer;
            } else if (jsReplacer.getClassName().equals("Array")) {
                propertyList = new ArrayList<>();
                long len = Types.toInteger(context, jsReplacer.get(context, "length"));
                for (long i = 0; i < len; ++i) {
                    Object v = jsReplacer.get(context, "" + i);
                    String item = null;
                    if (Types.type(v).equals("string")) {
                        item = (String) v;
                    } else if (Types.type(v).equals("number")) {
                        item = Types.toString(context, v);
                    } else if (Types.type(v).equals("object")) {
                        if (((JSObject) v).getClassName().equals("String") || ((JSObject) v).getClassName().equals("Number")) {
                            item = Types.toString(context, v);
                        }
                    }

                    if ((item != null) && !propertyList.contains(item)) {
                        propertyList.add(item);
                    }
                }
            }
        }

        Object space = args[2];

        if (space != Types.UNDEFINED) {
            if (space instanceof JSObject) {
                if (((JSObject) space).getClassName().equals("Number")) {
                    space = Types.toNumber(context, space);
                } else if (((JSObject) space).getClassName().equals("String")) {
                    space = Types.toString(context, space);
                }
            }
        }

        String gap = "";

        if (Types.type(space).equals("number")) {
            long spaceInt = Types.toInteger(context, space);
            if (spaceInt > 10) {
                spaceInt = 10;
            }
            for (long i = 0; i < spaceInt; ++i) {
                gap += " ";
            }
        } else if (Types.type(space).equals("string")) {
            String spaceStr = Types.toString(context, space);
            if (spaceStr.length() <= 10) {
                gap = spaceStr.toString();
            } else {
                gap = spaceStr.substring(0, 10);
            }
        }
        
        Object value = args[0];

        JSObject wrapper = BuiltinObject.newObject(context);
        wrapper.put(context, "", value, false);

        String strResult = str(context, stack, indent, gap, replacerFunction, propertyList, wrapper, "");
        return strResult == null ? Types.UNDEFINED : strResult;
    }

    protected String str(ExecutionContext context,
            List<Object> stack,
            String indent, String gap,
            JSFunction replacer, List<String> propertyList,
            JSObject holder, String key) {
        Object value = holder.get(context, key);

        if (value instanceof JSObject) {
            Object toJSON = ((JSObject) value).get(context, "toJSON");
            if (toJSON instanceof JSFunction) {
                value = context.call((JSFunction) toJSON, value, key);
            }
        }

        if (replacer != null) {
            value = context.call(replacer, holder, key, value);
        }

        if (value instanceof JSObject) {
            String objClass = ((JSObject) value).getClassName();
            if (objClass.equals("Number")) {
                value = Types.toNumber(context, value);
            } else if (objClass.equals("String")) {
                value = Types.toString(context, value);
            } else if (objClass.equals("Boolean")) {
                value = ((PrimitiveDynObject) value).getPrimitiveValue();
            }
        }

        if (value == null || value == Types.NULL) {
            return "null";
        }

        if (value == Boolean.TRUE) {
            return "true";
        }

        if (value == Boolean.FALSE) {
            return "false";
        }

        if (value instanceof String) {
            return quote((String) value);
        }

        if (value instanceof Number) {
            if (value instanceof Double) {
                if (((Double) value).isInfinite()) {
                    return "null";
                }
            }

            return Types.toString(context, value);
        }

        if ((value instanceof JSObject) && (!(value instanceof JSFunction))) {
            if (value instanceof DynArray) {
                return ja(context, stack, indent, gap, replacer, propertyList, (DynArray) value);
            } else {
                return jo(context, stack, indent, gap, replacer, propertyList, (JSObject) value);
            }
        }

        return null;
    }

    private String ja(ExecutionContext context,
            List<Object> stack,
            String indent, String gap,
            JSFunction replacer, List<String> propertyList,
            DynArray value) {
        if (stack.contains(value)) {
            throw new ThrowException(context, context.createTypeError("cyclic structure"));
        }
        stack.add(value);
        String stepback = indent;
        indent = indent + gap;

        List<String> partial = new ArrayList<>();

        long len = Types.toInteger(context, value.get(context, "length"));

        for (long i = 0; i < len; ++i) {
            String strP = str(context, stack, indent, gap, replacer, propertyList, value, "" + i);
            if (strP == null) {
                partial.add("null");
            } else {
                partial.add(strP);
            }
        }

        StringBuilder finalStr = new StringBuilder();

        if (partial.isEmpty()) {
            finalStr.append("[]");
        } else {
            StringBuilder properties = new StringBuilder();

            String separator = ",";
            if (!gap.isEmpty()) {
                separator = ",\n" + indent;
            }
            boolean first = true;
            for (String each : partial) {
                if (!first) {
                    properties.append(separator);
                }
                first = false;
                properties.append(each);
            }

            if (gap.isEmpty()) {
                finalStr.append("[").append(properties.toString()).append("]");
            } else {
                finalStr.append("[\n")
                        .append(indent)
                        .append(properties.toString()).append("\n")
                        .append(stepback).append("]");
            }
        }
        
        stack.remove(stack.size() - 1);

        return finalStr.toString();

    }

    private String jo(ExecutionContext context,
            List<Object> stack,
            String indent, String gap,
            JSFunction replacer, List<String> propertyList,
            JSObject value) {

        if (stack.contains(value)) {
            throw new ThrowException(context, context.createTypeError("cyclic structure"));
        }
        stack.add(value);
        String stepback = indent;
        indent = indent + gap;

        List<String> partial = new ArrayList<>();

        List<String> k = null;
        if (propertyList == null || propertyList.isEmpty()) {
            k = value.getOwnEnumerablePropertyNames().toList();
        } else {
            k = propertyList;
        }

        for (String p : k) {
            String strP = str(context, stack, indent, gap, replacer, propertyList, value, p);

            if (strP != null) {
                StringBuilder member = new StringBuilder();
                member.append(quote(p)).append(':');
                if (!gap.isEmpty()) {
                    member.append(' ');
                }
                member.append(strP);
                partial.add(member.toString());
            }
        }

        StringBuilder finalStr = new StringBuilder();

        if (partial.isEmpty()) {
            finalStr.append("{}");
        } else {
            StringBuilder properties = new StringBuilder();

            String separator = ",";
            if (!gap.isEmpty()) {
                separator = ",\n" + indent;
            }
            boolean first = true;
            for (String each : partial) {
                if (!first) {
                    properties.append(separator);
                }
                first = false;
                properties.append(each);
            }

            if (gap.isEmpty()) {
                finalStr.append("{").append(properties.toString()).append("}");
            } else {
                finalStr.append("{\n")
                        .append(indent)
                        .append(properties.toString()).append("\n")
                        .append(stepback).append("}");
            }
        }

        stack.remove(stack.size() - 1);

        return finalStr.toString();
    }

    protected String quote(String value) {
        StringBuilder product = new StringBuilder();
        product.append('"');

        int strLen = value.length();

        for (int i = 0; i < strLen; ++i) {
            char c = value.charAt(i);

            switch (c) {
            case '"':
            case '\\':
                product.append('\\').append(c);
                break;
            case '\b':
                product.append('\\').append('b');
                break;
            case '\f':
                product.append('\\').append('f');
                break;
            case '\n':
                product.append('\\').append('n');
                break;
            case '\r':
                product.append('\\').append('r');
                break;
            case '\t':
                product.append('\\').append('t');
                break;
            default:
                if (c < ' ') {
                    String hex = Integer.toHexString(c);
                    if (hex.length() == 1) {
                        hex = "000" + hex;
                    } else if (hex.length() == 2) {
                        hex = "00" + hex;
                    } else if (hex.length() == 3) {
                        hex = "0" + hex;
                    }
                    product.append('\\').append('u').append(hex);
                } else {
                    product.append(c);
                }
            }
        }

        product.append('"');
        return product.toString();
    }
}
