package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    protected String debugContext;

    public AbstractFunction(final GlobalContext globalContext, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(globalContext);
        this.formalParameters = formalParameters;
        this.scope = scope;
        this.strict = strict;
        setClassName("Function");
        // http://es5.github.com/#x15.3.3.2
        defineOwnProperty(null, "length",
                PropertyDescriptor.newDataPropertyDescriptor((long) formalParameters.length, false, false, false), false);

        if (strict) {
            final Object thrower = globalContext.getThrowTypeError();
            if (thrower != null) {
                defineOwnProperty(null, "caller",
                        PropertyDescriptor.newAccessorPropertyDescriptor(thrower, thrower), false);
                defineOwnProperty(null, "arguments",
                        PropertyDescriptor.newAccessorPropertyDescriptor(thrower, thrower), true);
            }
        }

        setPrototype(globalContext.getPrototypeFor("Function"));
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

        JSObject proto = (JSObject) o;

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
        return new DynObject(context.getGlobalContext());
    }

    public void setDebugContext(String debugContext) {
        this.debugContext = debugContext;
    }

    public String getDebugContext() {
        return this.debugContext;
    }
    
}
