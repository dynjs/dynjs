package org.dynjs.runtime.builtins.types.error;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.11.4.4
        if (!(self instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("'this' must be an object"));
        }

        JSObject jsSelf = (JSObject) self;

        Object nameObj = jsSelf.get(context, "name");
        String name = "";
        if (nameObj == Types.UNDEFINED) {
            name = "Error";
        } else {
            name = Types.toString(context, nameObj);
        }

        Object messageObj = jsSelf.get(context, "message");
        String message = "";
        if (messageObj == Types.UNDEFINED) {
            message = "";
        } else {
            message = Types.toString(context, messageObj);
        }

        if (nameObj == Types.UNDEFINED && messageObj == Types.UNDEFINED) {
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
