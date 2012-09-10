package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.GlobalObject;

public class ToTimeString extends AbstractDateTimeFormatter {
    public ToTimeString(GlobalObject globalObject) {
        super(globalObject, "HH:mm:ss 'GMT'Z (z)");
    }
}
