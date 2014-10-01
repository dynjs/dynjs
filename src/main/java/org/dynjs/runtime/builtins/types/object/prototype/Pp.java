package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;

import java.util.HashSet;
import java.util.Set;

public class Pp extends AbstractNativeFunction {

    public Pp(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) self;

        StringBuilder pp = new StringBuilder();
        pp(context, pp, "", jsObj, new HashSet<JSObject>());
        return pp.toString();
    }

    protected void pp(ExecutionContext context, StringBuilder out, String indent, JSObject jsObj, Set<JSObject> seen) {
        out.append(indent).append("[");
        Object ctorProp = jsObj.getProperty(context, "__ctor__");
        if (ctorProp instanceof PropertyDescriptor) {
            out.append(((PropertyDescriptor) ctorProp).getValue());
        } else {
            if (jsObj instanceof JSFunction) {
                out.append(jsObj.get(context, "name")).append("(..)");
            } else {
                out.append("unknown");
            }
        }
        seen.add(jsObj);

        out.append("@");
        out.append(System.identityHashCode(jsObj));

        if (jsObj.getPrototype() != null) {
            out.append("\n").append(indent + "  ").append("__proto__:\n");
            pp(context, out, indent + "    ", jsObj.getPrototype(), seen);
        }

        NameEnumerator enumerator = jsObj.getAllEnumerablePropertyNames();
        if (enumerator.hasNext()) {
            out.append("\n");

            while (enumerator.hasNext()) {
                String name = enumerator.next();
                out.append(indent + "  ").append(name).append(": {\n");
                Object v = jsObj.getProperty(context, name);
                if (v == Types.UNDEFINED) {
                    out.append("undefined");
                } else {
                    PropertyDescriptor prop = (PropertyDescriptor) v;

                    if (prop.hasValue()) {
                        out.append(indent + "    ").append("value: ");
                        v = prop.getValue();
                        if (v instanceof JSObject) {
                            if (seen.contains(v)) {
                                out.append("[circular]");
                            } else {
                                out.append( "\n" );
                                pp(context, out, indent + "      ", (JSObject) v, seen);
                            }
                        } else {
                            out.append(v);
                        }
                    }
                    if (prop.hasGet()) {
                        out.append(indent + "    ").append("get: ");
                        pp(context, out, indent + "      ", (JSObject) ((PropertyDescriptor) v).getGetter(), seen);
                    }

                    if (prop.hasSet()) {
                        out.append(indent + "    ").append("set: ");
                        pp(context, out, indent + "      ", (JSObject) ((PropertyDescriptor) v).getSetter(), seen);
                    }
                    out.append("\n" + indent + "  }").append("\n");
                }
            }
            out.append("\n");
            out.append(indent);
        }

        out.append("]");
    }

}
