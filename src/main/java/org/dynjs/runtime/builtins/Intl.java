package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.intl.Collator;
import org.dynjs.runtime.builtins.intl.DateTimeFormat;
import org.dynjs.runtime.builtins.intl.NumberFormat;

public class Intl extends DynObject {

    public Intl(GlobalObject globalObject) {
        super(globalObject);
        put(null, "Collator", new Collator(globalObject), false);
        put(null, "NumberFormat", new NumberFormat(globalObject), false);
        put(null, "DateTimeFormat", new DateTimeFormat(globalObject), false);
    }

}
