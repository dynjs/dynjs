package org.dynjs.runtime.builtins.types.error;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.11.4.4
        if (!(self instanceof JSObject)) {
            throw new ThrowException(context.createTypeError("'this' must be an object"));
        }

        JSObject jsSelf = (JSObject) self;

        Object name = jsSelf.get(context, "name");
        if (name == Types.UNDEFINED) {
            name = "Error";
        } else {
            name = Types.toString(context, name);
        }

        Object message = jsSelf.get(context, "message");
        if (message == Types.UNDEFINED) {
            message = "";
        } else {
            message = Types.toString(context, message);
        }

        if (name.toString().equals("") && message.toString().equals("")) {
            return "Error";
        }

        if (name.equals("")) {
            return message;
        }

        if (message.equals("")) {
            return name;
        }

        return name + ": " + message;
    }

}
