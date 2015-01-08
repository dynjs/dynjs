package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.intl.Collator;
import org.dynjs.runtime.builtins.intl.DateTimeFormat;
import org.dynjs.runtime.builtins.intl.NumberFormat;

public class Intl extends DynObject {

    public Intl(GlobalContext globalContext) {
        super(globalContext);
        put(null, "Collator", new Collator(globalContext), false);
        put(null, "NumberFormat", new NumberFormat(globalContext), false);
        put(null, "DateTimeFormat", new DateTimeFormat(globalContext), false);
    }

}
