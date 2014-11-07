package org.dynjs.runtime;

public interface JSFunction extends JSObject, JSCallable, JSCode {
    String[] getFormalParameters();

    LexicalEnvironment getScope();

    boolean hasInstance(ExecutionContext context, Object obj);

    JSObject createNewObject(ExecutionContext context);

    String getFileName();

    void setDebugContext(String context);

    String getDebugContext();

    void setSource(SourceProvider source);

    boolean isConstructor();

}
