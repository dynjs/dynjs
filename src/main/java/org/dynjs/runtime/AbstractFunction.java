package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.PropertyDescriptor.Names;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    protected String debugContext;

    public AbstractFunction(final GlobalObject globalObject, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(globalObject);
        this.formalParameters = formalParameters;
        this.scope = scope;
        this.strict = strict;
        setClassName("Function");
        PropertyDescriptor lengthDesc =  new PropertyDescriptor();
        lengthDesc.set(Names.VALUE, (long) formalParameters.length); // http://es5.github.com/#x15.3.3.2
        lengthDesc.set(Names.WRITABLE, false);
        lengthDesc.set(Names.CONFIGURABLE, false);
        lengthDesc.set(Names.ENUMERABLE, false);
        defineOwnProperty(null, "length", lengthDesc, false);

        if (strict) {
            final Object thrower = globalObject.get(null, "__throwTypeError");
            if (thrower != null) {
                PropertyDescriptor callerDesc = new PropertyDescriptor();
                callerDesc.set(Names.SET, thrower);
                callerDesc.set(Names.GET, thrower);
                callerDesc.set(Names.CONFIGURABLE, false);
                callerDesc.set(Names.ENUMERABLE, false);
                defineOwnProperty(null, "caller", callerDesc, false);

                PropertyDescriptor argDesc = new PropertyDescriptor();
                argDesc.set(Names.SET, thrower);
                argDesc.set(Names.GET, thrower);
                argDesc.set(Names.CONFIGURABLE, false);
                argDesc.set(Names.ENUMERABLE, false);
                defineOwnProperty(null, "arguments", argDesc, true);
            }
        }

        setPrototype(globalObject.getPrototypeFor("Function"));
    }

    public LexicalEnvironment getScope() {
        return this.scope;
    }

    public void setScope(LexicalEnvironment scope) {
        this.scope = scope;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public boolean isConstructor() {
        return true;
    }

    @Override
    public String[] getFormalParameters() {
        return this.formalParameters;
    }

    protected void setFormalParamters(String[] formalParameters) {
        this.formalParameters = formalParameters;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public Object get(ExecutionContext context, String name) {
        // 15.3.5.4
        if (name.equals("caller") && this.strict) {
            throw new ThrowException(context, context.createTypeError("may not reference 'caller'"));
        }
        return super.get(context, name);
    }

    @Override
    public boolean hasInstance(ExecutionContext context, Object v) {
        if (!(v instanceof JSObject)) {
            return false;
        }

        Object o = get(null, "prototype");
        if (!(o instanceof JSObject)) {

            throw new ThrowException(context, context.createTypeError("prototype must be an object"));
        }

        JSObject proto = (JSObject) get(null, "prototype");

        if (proto == null || v == Types.UNDEFINED) {
            return false;
        }

        while (true) {
            v = ((JSObject) v).getPrototype();
            if (v == null || v == Types.UNDEFINED) {
                return false;
            }
            if (v == proto) {
                return true;
            }
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynObject(context.getGlobalObject());
    }

    public void setDebugContext(String debugContext) {
        this.debugContext = debugContext;
    }

    public String getDebugContext() {
        return this.debugContext;
    }
    
}
