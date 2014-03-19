package org.dynjs.runtime;

public class ThreadContext {

    public static final int DEFAULT_EXECUTION_CTX_SIZE = 16;
    private ExecutionContext[] executionContextStack = new ExecutionContext[DEFAULT_EXECUTION_CTX_SIZE];
    private int executionContextIndex = -1;

    public ExecutionContext getCurrentContext() {
        if (executionContextIndex < 0) {
            return null;
        }

        return executionContextStack[executionContextIndex];
    }

    public void pushContext(ExecutionContext executionContext) {
        int index = ++executionContextIndex;
        ExecutionContext[] stack = executionContextStack;
        stack[index] = executionContext;
        if (index + 1 == stack.length) {
            expandExecutionContextStack();
        }
    }

    private void expandExecutionContextStack() {
        int newSize = executionContextStack.length * 2;
        final ExecutionContext[] newStack = new ExecutionContext[newSize];
        System.arraycopy(executionContextStack, 0, newStack, 0, executionContextStack.length);

        executionContextStack = newStack;
    }

    public void popContext() {
        executionContextStack[executionContextIndex--] = null;
    }

    public ExecutionContext getParentContext() {
        if (executionContextIndex < 1) {
            return null;
        }
        return  executionContextStack[executionContextIndex - 1];
    }
}
