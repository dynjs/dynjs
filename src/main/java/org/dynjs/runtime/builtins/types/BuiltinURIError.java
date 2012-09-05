package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinURIError extends AbstractBuiltinNativeError {

    public BuiltinURIError(GlobalObject globalObject) {
        super(globalObject, "URIError");
    }

}
