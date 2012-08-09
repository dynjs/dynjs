package org.dynjs.runtime;

public interface JSEval extends JSCode {
    Object evaluate(ExecutionContext context);
}
