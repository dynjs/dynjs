package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.GlobalObject;

public class ToLocaleString extends DateTimeFormatter {
    public ToLocaleString(GlobalObject globalObject) {
        super(globalObject, "EEE MMM dd HH:mm:ss YYYY");
    }
}
