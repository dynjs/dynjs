package org.dynjs.runtime.loader;

public class Builtin {

    private final String bindingName;
    private final Object boundObject;

    public Builtin(String bindingName, Object boundObject) {
        this.bindingName = bindingName;
        this.boundObject = boundObject;
    }

    public String getBindingName() {
        return bindingName;
    }

    public Object getBoundObject() {
        return boundObject;
    }
}
