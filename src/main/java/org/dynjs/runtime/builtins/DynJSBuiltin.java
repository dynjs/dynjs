package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;

public class DynJSBuiltin extends DynObject {
    public DynJSBuiltin(GlobalObject globalObject, DynJS runtime) {
        super(globalObject);
    }
}
