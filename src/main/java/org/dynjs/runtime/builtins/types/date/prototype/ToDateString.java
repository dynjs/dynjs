package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.GlobalObject;

public class ToDateString extends AbstractDateTimeFormatter {

    public ToDateString(GlobalObject globalObject) {
        super(globalObject, "EEE MMM dd YYYY");
    }

}
