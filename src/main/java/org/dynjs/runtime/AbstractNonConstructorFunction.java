package org.dynjs.runtime;

public abstract class AbstractNonConstructorFunction extends AbstractNativeFunction {

    public AbstractNonConstructorFunction(GlobalObject globalObject, String... formalParameters) {
        super(globalObject, true, formalParameters);
    }
    
    public AbstractNonConstructorFunction(GlobalObject globalObject, boolean strict, String... formalParameters) {
        super(globalObject, true, formalParameters);
    }
    
    public boolean isConstructor() {
        return false;
    }

}
