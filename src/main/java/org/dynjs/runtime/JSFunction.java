package org.dynjs.runtime;

public interface JSFunction extends JSObject, JSCallable, JSCode {
    String[] getFormalParameters();

    LexicalEnvironment getScope();
    void setScope(LexicalEnvironment scope);

    boolean hasInstance(ExecutionContext context, Object obj);

    JSObject createNewObject(ExecutionContext context);

    String getFileName();

    void setDebugContext(String context);

    String getDebugContext();

    boolean isConstructor();

}
