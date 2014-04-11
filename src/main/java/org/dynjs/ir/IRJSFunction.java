package org.dynjs.ir;

import org.dynjs.parser.ast.FunctionDescriptor;

public class IRJSFunction {
    private final Scope scope;
    private final FunctionDescriptor descriptor;
    private final boolean strict;

    public IRJSFunction(Scope scope, FunctionDescriptor descriptor, boolean strict) {
        this.scope = scope;
        this.descriptor = descriptor;
        this.strict = strict;
    }
}
