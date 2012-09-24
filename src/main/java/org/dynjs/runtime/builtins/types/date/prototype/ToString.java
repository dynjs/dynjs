package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.GlobalObject;

public class ToString extends DateTimeFormatter {
    public ToString(GlobalObject globalObject) {
        super(globalObject, "EEE MMM dd YYYY HH:mm:ss 'GMT'Z (z)");
    }
}
