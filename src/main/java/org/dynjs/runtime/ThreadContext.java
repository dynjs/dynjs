package org.dynjs.runtime;

import java.util.List;

/**
 * Created by enebo on 3/20/14.
 */
public class ThreadContext {
    private static int STARTING_CONTEXT_SIZE = 300;
    private ExecutionContext[] contexts = new ExecutionContext[STARTING_CONTEXT_SIZE];
    private int contextIndex = -1;

    public ThreadContext() {
        int size= contexts.length;

        for (int i = 0; i < size; i++) {
            contexts[i] = new ExecutionContext();
        }
    }

    private void expandContextStack() {
        int newSize = contexts.length * 2;
        contexts = fillNewContextStack(new ExecutionContext[newSize], newSize);
    }

    private ExecutionContext[] fillNewContextStack(ExecutionContext[] newContexts, int newSize) {
        System.arraycopy(contexts, 0, newContexts, 0, contexts.length);

        for (int i = contexts.length; i < newSize; i++) {
            newContexts[i] = new ExecutionContext();
        }

        return newContexts;
    }

    public ExecutionContext getCurrentContext() {
        return contexts[contextIndex];
    }

    public ExecutionContext getParentContext() {
        return contextIndex > 0 ? contexts[contextIndex - 1] : null;
    }

    public ExecutionContext getLowestContext() {
        return contexts[0];
    }

    public ExecutionContext pushContext(DynJS runtime, LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, Object thisBinding, boolean strict) {
        int index = ++this.contextIndex;
        ExecutionContext[] stack = contexts;
        if (index + 1 >= stack.length) {
            expandContextStack();
        }
        ExecutionContext current = contexts[contextIndex];
        current.init(runtime, lexicalEnvironment, variableEnvironment, thisBinding, strict);
        return current;
    }

    public void popContext() {
        ExecutionContext[] stack = contexts;
        stack[contextIndex].reset();
        contextIndex--;
    }

    public void collectStackElements(List<StackElement> elements) {
        for (int i = contextIndex-1; i >= 0; i--) {
            ExecutionContext current = contexts[i];

            elements.add(new StackElement(current.getFileName(), current.getLineNumber(), current.getDebugContext()));
        }
    }
}
