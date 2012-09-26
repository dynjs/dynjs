package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class AbstractJavascriptFunction extends AbstractFunction {

    public AbstractJavascriptFunction(final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this(null, scope, strict, formalParameters);
    }

    public AbstractJavascriptFunction(final Statement body, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(body, scope, strict, formalParameters);

        final DynObject proto = new DynObject(scope.getGlobalObject());
        proto.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", AbstractJavascriptFunction.this);
                set("Writable", true);
                set("Enumerable", false);
                set("Configurable", true);
            }
        }, false);
        defineOwnProperty(null, "prototype", new PropertyDescriptor() {
            {
                set( "Value", proto );
                set( "Writable", true );
                set( "Enumerable", false );
                set( "Configurable", false );
            }
        }, false);
    }

}
