package org.dynjs.runtime;

public interface JSFunction extends JSObject, JSCallable, JSCode {
    String[] getFormalParameters();
    LexicalEnvironment getScope();
    Object construct(ExecutionContext context, Object...args);
}
