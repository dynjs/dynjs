package org.dynjs.ir;

public class IRJSFunction {
    private final Scope scope;
    private final String identifier;
    private final boolean strict;

    public IRJSFunction(Scope scope, String identifier, String[] formalParameters, boolean strict) {
        this.scope = scope;
        this.identifier = identifier;
        this.strict = strict;
//        this.scope.pushFunction(this);
    }
}
