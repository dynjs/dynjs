package org.dynjs.runtime;

import java.util.List;

/**
 * Created by enebo on 3/20/14.
 */
public class ThreadContext {
    private static int STARTING_CONTEXT_SIZE = 300;
    private ExecutionContext[] contexts = new ExecutionContext[STARTING_CONTEXT_SIZE];
    private int contextIndex = 0;

    public ExecutionContext getCurrentContext() {
        return null;
    }

    public ExecutionContext getPreviousContext() {
        return null;
    }

    public ExecutionContext getLowestContext() {
        return contexts[0];
    }

    public void pushContext(DynJS runtime, LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, Object thisBinding, boolean strict) {
        ExecutionContext current = contexts[contextIndex++];
        current.init(runtime, lexicalEnvironment, variableEnvironment, thisBinding, strict);
    }

    public void popContext() {
        ExecutionContext current = contexts[contextIndex++];
        current.reset();
        contextIndex--;

    }

    public void collectStackElements(List<StackElement> elements) {
        for (int i = contextIndex; i >= 0; i++) {
            ExecutionContext current = contexts[i];

            elements.add(new StackElement(current.getFileName(), current.getLineNumber(), current.getDebugContext()));
        }
    }
}
