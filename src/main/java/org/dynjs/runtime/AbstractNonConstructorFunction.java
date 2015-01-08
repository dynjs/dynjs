package org.dynjs.runtime;

public abstract class AbstractNonConstructorFunction extends AbstractNativeFunction {

    public AbstractNonConstructorFunction(GlobalContext globalContext, String... formalParameters) {
        super(globalContext, true, formalParameters);
    }
    
    public AbstractNonConstructorFunction(GlobalContext globalContext, boolean strict, String... formalParameters) {
        super(globalContext, true, formalParameters);
    }
    
    public boolean isConstructor() {
        return false;
    }

}
