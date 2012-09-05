package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class AbstractJavascriptFunction extends AbstractFunction {

    public AbstractJavascriptFunction(final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this(null, scope, strict, formalParameters);
    }

    public AbstractJavascriptFunction(final Statement body, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(body, scope, strict, formalParameters);

        DynObject proto = new DynObject(scope.getGlobalObject());
        proto.put(null, "constructor", this, false);

        put(null, "prototype", proto, false);
    }

}
