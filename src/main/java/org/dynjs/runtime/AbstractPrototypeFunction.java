package org.dynjs.runtime;

public abstract class AbstractPrototypeFunction extends AbstractNativeFunction {

    public AbstractPrototypeFunction(GlobalObject globalObject, String... formalParameters) {
        super(globalObject, true, formalParameters);
    }
    
    public AbstractPrototypeFunction(GlobalObject globalObject, boolean strict, String... formalParameters) {
        super(globalObject, true, formalParameters);
    }
    
    public boolean isConstructor() {
        return false;
    }

}
