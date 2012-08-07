package org.dynjs.runtime;

public interface JSFunction extends JSObject, JSCallable {
    String[] getFormalParameters();
}
