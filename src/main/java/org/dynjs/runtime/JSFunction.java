package org.dynjs.runtime;

public interface JSFunction extends JSObject, JSCallable, JSCode {
    String[] getFormalParameters();

    LexicalEnvironment getScope();

    boolean hasInstance(Object obj);

    JSObject createNewObject(ExecutionContext context);

    String getFileName();

    void setDebugContext(String context);

    String getDebugContext();

}
